package com.atypon.processorsimulator.strategy;


import com.atypon.processorsimulator.model.Processor;
import com.atypon.processorsimulator.model.Task;

import java.util.Comparator;
import java.util.List;

public class PrioritySchedulingStrategy implements SchedulingStrategy {
    @Override
    public void scheduleTasks(List<Processor> processors, List<Task> waitingTasks) {
        waitingTasks.sort(Comparator.comparingInt(Task::getPriority).reversed()
                .thenComparing(Comparator.comparingInt(Task::getRequestedTime).reversed())
                .thenComparingInt(Task::getId));

        for (Processor processor : processors) {
            if (processor.isIdle() && !waitingTasks.isEmpty()) {
                processor.assignTask(waitingTasks.remove(0));
            }
        }

        // Interrupt low-priority tasks if a high-priority task is available
        for (Processor processor : processors) {
            if (processor.isBusy() && processor.getTask().isLowPriority()) {
                Task highPriorityTask = getFirstHighPriorityTask(waitingTasks);
                if (highPriorityTask != null) {
                    replaceTask(processor, highPriorityTask, waitingTasks);
                }
            }
        }
    }

    private Task getFirstHighPriorityTask(List<Task> waitingTasks) {
        return waitingTasks.stream().filter(Task::isHighPriority).findFirst().orElse(null);
    }

    private void replaceTask(Processor processor, Task newTask, List<Task> waitingTasks) {
        System.out.println("Processor id = " + processor.getId()
                + " has switched from task id = " + processor.getTask().getId()
                + " to task id = " + newTask.getId());
        waitingTasks.add(processor.removeTask());
        processor.assignTask(newTask);
        waitingTasks.remove(newTask);
    }
}