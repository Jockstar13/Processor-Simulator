package com.atypon.processorsimulator.model;

import com.atypon.processorsimulator.input.IdGenerator;
import com.atypon.processorsimulator.states.ProcessorState;
import com.atypon.processorsimulator.states.TaskState;

public class Processor {
    private final int id;
    private Task task;
    private ProcessorState state;

    public Processor() {
        this.id = IdGenerator.getNextProcessorId();
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
        //   System.out.println("Processor id = " + id + " is executing task " + task.getId());//
        task.run();
        if (task.isFinished()) {
            System.out.println("Processor id = " + id + " has completed task id " + task.getId() + " and is now idle");
            task.setFinished(); // Move this line here
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
