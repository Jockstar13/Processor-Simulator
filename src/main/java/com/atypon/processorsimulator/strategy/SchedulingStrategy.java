package com.atypon.processorsimulator.strategy;

import com.atypon.processorsimulator.model.Processor;
import com.atypon.processorsimulator.model.Task;

import java.util.List;

public interface SchedulingStrategy {
    void scheduleTasks(List<Processor> processors, List<Task> waitingTasks);
}