# Processor Simulator

## Overview

This project is a processor simulator that allows you to manage tasks and processors, scheduling tasks based on specified strategies.

## File Structure

### Resource Files

#### `input.txt`

This file should be placed in the `src/main/resources` directory. It contains:

- The number of processors.
- The number of cycles to simulate.
- The path to the task file (`task.txt`).

**Example `input.txt` content:**

1. [ ] 4
2. [ ] 10
3. [ ] task.txt


- `4` specifies the number of processors.
- `10` specifies the number of simulation cycles.
- `task.txt` is the name of the file containing task details.

#### `task.txt`

This file should also be placed in the `src/main/resources` directory. It contains:

- The number of tasks.
- Each task's details: creation time, requested time, and priority.

**Example `task.txt` content:**

1. [ ] 3
2. [ ] 0 5 high
3. [ ] 2 3 low
4. [ ] 5 2 high

- `3` specifies the number of tasks.
- Each line following represents a task with:

    - Creation time  Requested time  Priority(`high` or `low`)

## Documentation

For a detailed explanation of the project, including setup and usage instructions, please refer to the documentation:

- **[Project Documentation PDF](Documentation.pdf)**
- **[Project Overview Video](https://youtu.be/750pPlgeL6M)**


