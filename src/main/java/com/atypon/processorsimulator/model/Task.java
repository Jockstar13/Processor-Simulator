package com.atypon.processorsimulator.model;

import com.atypon.processorsimulator.input.IdGenerator;
import com.atypon.processorsimulator.states.TaskState;

public final class Task {
    private final int id;
    private final int creationTime;
    private final int requestedTime;
    private final int priority;
    private TaskState state;
    private int remainingTime;

    public Task(int creationTime, int requestedTime, int priority) {
        this.id = IdGenerator.getNextTaskId();
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

    public TaskState getState() {
        return state;
    }

    public void setExecuting() {
        this.state = TaskState.EXECUTING;
    }

    public void setSleeping() {
        this.state = TaskState.ASLEEP;
    }

    public void setFinished() {
        this.state = TaskState.FINISHED;
    }

    public boolean isFinished() {
        return remainingTime <= 0;
    }

    public void run() {
        if (remainingTime > 0) {
            remainingTime--;
        }
    }
    public boolean isHighPriority() {
        return priority == 1;
    }

    public boolean isLowPriority() {
        return priority == 0;
    }
}
