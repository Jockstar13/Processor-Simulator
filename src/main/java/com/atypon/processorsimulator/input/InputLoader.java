package com.atypon.processorsimulator.input;

import com.atypon.processorsimulator.model.Processor;
import com.atypon.processorsimulator.model.Task;

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InputLoader {
    private int numberOfProcessors;
    private int numberOfCycles;
    private final List<Processor> processors;
    private final List<Task> incomingTasks;
    private static final Logger logger = Logger.getLogger(InputLoader.class.getName());

    public InputLoader(List<Processor> processors, List<Task> incomingTasks) {
        this.processors = processors;
        this.incomingTasks = incomingTasks;
    }

    public List<Task> getIncomingTasks() {
        return incomingTasks;
    }

    public void loadInput() {
        incomingTasks.clear();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("input.txt");
             Scanner sc = new Scanner(inputStream)) {

            numberOfProcessors = Integer.parseInt(sc.nextLine().trim());
            numberOfCycles = Integer.parseInt(sc.nextLine().trim());
            String taskFilePath = sc.nextLine().trim();
            loadTaskFile(taskFilePath);

            // Initialize processors based on the loaded number
            for (int i = 0; i < numberOfProcessors; i++) {
                processors.add(new Processor());
            }

            // Log the number of processors and cycles
            System.out.println("Loaded number of processors: " + numberOfProcessors);
            System.out.println("Loaded number of cycles: " + numberOfCycles);
            System.out.println("Loaded " + incomingTasks.size() + " tasks.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Input file not found or could not be loaded!", e);
        }
    }

    private void loadTaskFile(String taskFilePath) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(taskFilePath);
             Scanner sc = new Scanner(inputStream)) {

            int numberOfTasks = Integer.parseInt(sc.nextLine().trim());
            while (sc.hasNext()) {
                String[] tokens = sc.nextLine().trim().split(" ");
                int priority = tokens[2].equalsIgnoreCase("low") ? 0 : 1;
                Task task = new Task(
                        Integer.parseInt(tokens[0]), // Creation Time
                        Integer.parseInt(tokens[1]), // Requested Time
                        priority
                );
                incomingTasks.add(task);

                // Log each task as it's loaded
                System.out.println("Loaded task: " + task.getId() + " with creation time " + task.getCreationTime());
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Task file not found or could not be loaded!", e);
        }
    }

    public int getNumberOfCycles() {
        return numberOfCycles;
    }

    public int getNumberOfProcessors() {
        return numberOfProcessors;
    }
}
