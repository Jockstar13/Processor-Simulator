package com.atypon.processorsimulator.core;

import com.atypon.processorsimulator.model.Processor;
import com.atypon.processorsimulator.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class Scheduler {
    private final ProcessorSimulator simulator;

    public Scheduler(ProcessorSimulator simulator) {
        this.simulator = simulator;
    }

    public void run() {
        while (simulator.getCurrentCycle() <= simulator.getInputLoader().getNumberOfCycles()) {
            logCycleStart();
            processIncomingTasks();
            simulator.getSchedulingStrategy().scheduleTasks(simulator.getProcessors(), simulator.getWaitingTasks());
            executeTasks();
            logCycleIfEmpty();
            logCycleEnd();
            simulator.incrementCurrentCycle();
            sleepForOneSecond();
        }
        logProgramEnd();
    }

    private static void sleepForOneSecond() {
        try {
            TimeUnit.SECONDS.sleep(1); // Adding a delay of 1 second for each cycle
        } catch (InterruptedException e) {
            ProcessorSimulator.getLogger().log(Level.WARNING, "Cycle was interrupted!", e);
            Thread.currentThread().interrupt();
        }
    }

    private void logCycleStart() {
        System.out.println("At cycle = " + simulator.getCurrentCycle());
    }

    private void logCycleEnd() {
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println();
    }

    private void logProgramEnd() {
        System.out.println("PROGRAM IS FINISHED.");
    }

    private void logCycleIfEmpty() {
        if (simulator.getWaitingTasks().isEmpty() && allProcessorsIdle()) {
            System.out.println("All tasks finished execution");
        }
    }

    private boolean allProcessorsIdle() {
        return simulator.getProcessors().stream().allMatch(Processor::isIdle);
    }

    private void processIncomingTasks() {
        List<Task> arrivingTasks = new ArrayList<>();
        for (Task task : simulator.getIncomingTasks()) {
            if (task.getCreationTime() == simulator.getCurrentCycle()) {
                arrivingTasks.add(task);
                System.out.println("Task " + task.getId() + " has arrived at cycle " + simulator.getCurrentCycle());
            }
        }
        simulator.getIncomingTasks().removeAll(arrivingTasks);
        simulator.getWaitingTasks().addAll(arrivingTasks);
    }

    private void executeTasks() {
        for (Processor processor : simulator.getProcessors()) {
            if (processor.isBusy()) {
                System.out.println("Processor id = " + processor.getId() + " is executing task " + processor.getTask().getId());
                processor.executeTask();
            }
        }
    }
}