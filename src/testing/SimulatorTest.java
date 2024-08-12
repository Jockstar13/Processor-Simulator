package testing;

import processSim.ProcessorSimulator;

public class SimulatorTest
{
    public static void main(String[] args)
    {
        // make input.txt file  and inside input.txt put on  line1 number of processors,
        // line 2 number of cycles and the path of task file on the third line.
        ProcessorSimulator simulator = ProcessorSimulator.getInstance();
        simulator.run();
    }
}
