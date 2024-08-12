package com.atypon.processorsimulator.core;

import com.atypon.processorsimulator.input.InputLoader;
import com.atypon.processorsimulator.model.Processor;
import com.atypon.processorsimulator.model.Task;
import com.atypon.processorsimulator.strategy.SchedulingStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ProcessorSimulator implements Simulator {
    private int currentCycle = 0;
    private final List<Processor> processors = new ArrayList<>();
    private final List<Task> incomingTasks = new ArrayList<>();
    private final List<Task> waitingTasks = new ArrayList<>();
    private static ProcessorSimulator simulator = null;
    private static final Logger logger = Logger.getLogger(ProcessorSimulator.class.getName());

    private final InputLoader inputLoader;
    private final SchedulingStrategy schedulingStrategy;
    private final Scheduler scheduler;

    private ProcessorSimulator(InputLoader inputLoader, SchedulingStrategy schedulingStrategy, List<Processor> processors, List<Task> tasks) {
        this.inputLoader = inputLoader;
        this.schedulingStrategy = schedulingStrategy;
        this.processors.addAll(processors);
        this.incomingTasks.addAll(tasks);
        this.scheduler = new Scheduler(this);
    }

    public static ProcessorSimulator getInstance(InputLoader inputLoader, SchedulingStrategy schedulingStrategy, List<Processor> processors, List<Task> tasks) {
        if (simulator == null) {
            simulator = new ProcessorSimulator(inputLoader, schedulingStrategy, processors, tasks);
        }
        return simulator;
    }

    public void run() {
        scheduler.run();
    }

    // Getter methods for Scheduler to access
    public int getCurrentCycle() {
        return currentCycle;
    }

    public void incrementCurrentCycle() {
        currentCycle++;
    }

    public List<Processor> getProcessors() {
        return processors;
    }

    public List<Task> getIncomingTasks() {
        return incomingTasks;
    }

    public List<Task> getWaitingTasks() {
        return waitingTasks;
    }

    public InputLoader getInputLoader() {
        return inputLoader;
    }

    public SchedulingStrategy getSchedulingStrategy() {
        return schedulingStrategy;
    }

    public static Logger getLogger() {
        return logger;
    }
}
