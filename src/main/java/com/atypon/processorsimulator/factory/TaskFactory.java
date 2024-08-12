package com.atypon.processorsimulator.factory;


import com.atypon.processorsimulator.model.Task;

public class TaskFactory {
    public static Task createTask(int creationTime, int requestedTime, int priority) {
        return new Task(creationTime, requestedTime, priority);
    }
}