package com.atypon.processorsimulator.Launcher;

import com.atypon.processorsimulator.core.ProcessorSimulator;
import com.atypon.processorsimulator.input.InputLoader;
import com.atypon.processorsimulator.model.Processor;
import com.atypon.processorsimulator.model.Task;
import com.atypon.processorsimulator.strategy.PrioritySchedulingStrategy;
import com.atypon.processorsimulator.strategy.SchedulingStrategy;

import java.util.ArrayList;
import java.util.List;

public class SimulatorTest {
    public static void main(String[] args) {
        List<Processor> processors = new ArrayList<>();
        List<Task> incomingTasks = new ArrayList<>();

        InputLoader inputLoader = new InputLoader(processors, incomingTasks);
        inputLoader.loadInput();

        SchedulingStrategy schedulingStrategy = new PrioritySchedulingStrategy();

        ProcessorSimulator simulator = ProcessorSimulator.getInstance(inputLoader, schedulingStrategy, processors, incomingTasks);

        simulator.run();
    }
}
