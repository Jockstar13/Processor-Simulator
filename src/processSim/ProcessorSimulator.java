package processSim;

import states.ProcessorState;
import states.TaskState;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ProcessorSimulator implements Simulator {
    private static int currentCycle = 0;
    private static int numberOfProcessors;
    private static int numberOfTasks;
    private static int numberOfCycles;
    private static final List<Processor> processors = new ArrayList<>();
    private static final List<Task> incomingTasks = new ArrayList<>();
    private static final List<Task> completedTasks = new ArrayList<>();
    private static final List<Task> waitingTasks = new ArrayList<>();
    private static ProcessorSimulator simulator = null;
    private static final Logger logger = Logger.getLogger(ProcessorSimulator.class.getName());

    private ProcessorSimulator() {}

    public static ProcessorSimulator getInstance() {
        if (simulator == null) {
            simulator = new ProcessorSimulator();
        }
        return simulator;
    }

    public void run() {
        loadInput();
        Scheduler.run();
    }

    public void loadInput() {
        try (Scanner sc = new Scanner(new File("input.txt"))) {
            numberOfProcessors = Integer.parseInt(sc.nextLine().trim());
            for (int i = 0; i < numberOfProcessors; i++) {
                processors.add(new Processor());
            }
            numberOfCycles = Integer.parseInt(sc.nextLine().trim());
            loadTaskFile(sc.nextLine().trim());
            incomingTasks.sort(Comparator.comparingInt(Task::getCreationTime));
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "Input file is not found!", e);
        }
    }

    private void loadTaskFile(String taskFilePath) {
        try (Scanner sc = new Scanner(new File(taskFilePath))) {
            numberOfTasks = Integer.parseInt(sc.nextLine().trim());
            while (sc.hasNext()) {
                String[] tokens = sc.nextLine().trim().split(" ");
                int priority = tokens[2].equalsIgnoreCase("low") ? 0 : 1;
                incomingTasks.add(new Task(
                        Integer.parseInt(tokens[0]),
                        Integer.parseInt(tokens[1]),
                        priority
                ));
            }
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "Task file is not found!", e);
        }
    }

    private static class Scheduler {
        public static void run() {
            while (currentCycle <= numberOfCycles) {
                logCycleStart();
                processIncomingTasks();
                scheduleTasks();
                executeTasks();
                logCycleIfEmpty();
                logCycleEnd();
                currentCycle++;
                sleepForOneSecond();
            }
            logProgramEnd();
        }

        private static void logCycleIfEmpty(){
            if (waitingTasks.isEmpty() && allProcessorsIdle()) {
                System.out.println("All tasks finished execution");
            }
        }
        private static boolean allProcessorsIdle() {
            return processors.stream().allMatch(Processor::isIdle);
        }
        private static void sleepForOneSecond() {
            try {
                TimeUnit.SECONDS.sleep(1); // Adding a delay of 1 second for each cycle
            } catch (InterruptedException e) {
                logger.log(Level.WARNING, "Cycle was interrupted!", e);
                Thread.currentThread().interrupt();
            }
        }
        private static void logCycleStart() {
            System.out.println("At cycle = " + currentCycle);
        }

        private static void logCycleEnd() {
            System.out.println("------------------------------------------------------------------------------------");
            System.out.println();
        }

        private static void logProgramEnd() {
            System.out.println("PROGRAM IS FINISHED.");
        }

        private static void processIncomingTasks() {
            List<Task> arrivingTasks = incomingTasks.stream()
                    .filter(task -> task.getCreationTime() == currentCycle)
                    .collect(Collectors.toList());

            incomingTasks.removeAll(arrivingTasks);
            waitingTasks.addAll(arrivingTasks);

            arrivingTasks.forEach(task ->
                    System.out.println("Task " + task.getId() + " has arrived"));
        }

        private static void scheduleTasks() {
            tieBreaking();
            assignTaskForAsleepProcessors();
            interruptLowPriorityTask();
        }

        private static void tieBreaking() {
            waitingTasks.sort(Comparator.comparingInt(Task::getPriority).reversed()
                    .thenComparing(Comparator.comparingInt(Task::getRequestedTime).reversed())
                    .thenComparingInt(Task::getId));
        }

        public static void assignTaskForAsleepProcessors() {
            processors.stream()
                    .filter(Processor::isIdle)
                    .forEach(processor -> {
                        if (!waitingTasks.isEmpty()) {
                            processor.assignTask(waitingTasks.remove(0));
                        }
                    });
        }

        public static void interruptLowPriorityTask() {
            processors.stream()
                    .filter(Processor::isBusy)
                    .filter(processor -> processor.getTask().isLowPriority())
                    .forEach(processor -> {
                        Task highPriorityTask = getFirstHighPriorityTask();
                        if (highPriorityTask != null) {
                            replaceTask(processor, highPriorityTask);
                        }
                    });
        }

        private static void executeTasks() {
            processors.stream()
                    .filter(Processor::isBusy)
                    .forEach(Processor::executeTask);
        }

        private static Task getFirstHighPriorityTask() {
            return waitingTasks.stream().filter(Task::isHighPriority).findFirst().orElse(null);
        }

        private static void replaceTask(Processor processor, Task newTask) {
            System.out.println("Processor id = " + processor.getId()
                    + " has switched from task id = " + processor.getTask().getId()
                    + " to task id = " + newTask.getId());
            waitingTasks.add(processor.removeTask());
            processor.assignTask(newTask);
            waitingTasks.remove(newTask);
        }
    }

    private static class Processor {
        private Task task;
        private static int processorIdCounter = 1;
        private final int id;
        private ProcessorState state;

        public Processor() {
            this.id = processorIdCounter++;
            this.state = ProcessorState.ASLEEP;
        }

        public int getId() {
            return id;
        }

        public Task getTask() {
            return task;
        }

        public void assignTask(Task task) {
            this.task = task;
            task.setExecuting();
            this.state = ProcessorState.ACTIVE;
            System.out.println("Processor id = " + this.id + " has been assigned task id = " + task.getId());
        }

        public void executeTask() {
            if (task == null) {
                throw new IllegalStateException("No task assigned to processor id = " + id);
            }
            System.out.println("Processor id = " + id + " is executing task " + task.getId());
            task.run();
            if (task.isFinished()) {
                task.setFinished();
                completedTasks.add(task);
                System.out.println("Processor id = " + id + " has completed task id " + task.getId() + " and is now idle");
                task = null;
                state = ProcessorState.ASLEEP;
            }
        }

        public boolean isIdle() {
            return state == ProcessorState.ASLEEP;
        }

        public boolean isBusy() {
            return state == ProcessorState.ACTIVE;
        }

        public Task removeTask() {
            Task oldTask = task;
            task.setSleeping();
            task = null;
            state = ProcessorState.ASLEEP;
            return oldTask;
        }
    }

    private static class Task {
        private static int taskIdCounter = 1;
        private final int id;
        private final int creationTime;
        private final int requestedTime;
        private final int priority;
        private TaskState state;
        private int remainingTime;

        public Task(int creationTime, int requestedTime, int priority) {
            this.id = taskIdCounter++;
            this.creationTime = creationTime;
            this.requestedTime = requestedTime;
            this.priority = priority;
            this.state = TaskState.ASLEEP;
            this.remainingTime = requestedTime;
        }

        public int getId() {
            return id;
        }

        public int getCreationTime() {
            return creationTime;
        }

        public int getRequestedTime() {
            return requestedTime;
        }

        public int getPriority() {
            return priority;
        }

        public void setFinished() {
            state = TaskState.FINISHED;
        }

        public void setSleeping() {
            state = TaskState.ASLEEP;
        }

        public void setExecuting() {
            state = TaskState.EXECUTING;
        }

        public boolean isFinished() {
            return remainingTime == 0;
        }

        public void run() {
            if (remainingTime > 0) {
                remainingTime--;
            } else {
                throw new IllegalStateException("No remaining time for task id = " + id);
            }
        }

        public boolean isHighPriority() {
            return priority == 1;
        }

        public boolean isLowPriority() {
            return priority == 0;
        }
    }
}
