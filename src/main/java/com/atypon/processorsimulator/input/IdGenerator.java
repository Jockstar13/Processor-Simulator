package com.atypon.processorsimulator.input;


public final class IdGenerator {
    private static int processorIdCounter = 1;
    private static int taskIdCounter = 1;

    private IdGenerator() {}

    public static int getNextProcessorId() {
        return processorIdCounter++;
    }

    public static int getNextTaskId() {
        return taskIdCounter++;
    }
}
