package com.atypon.processorsimulator.factory;


import com.atypon.processorsimulator.model.Processor;

public class ProcessorFactory {
    public static Processor createProcessor() {
        return new Processor();
    }
}