package com.example.optimizedschedule;

import com.example.optimizedschedule.taskListHandeling.Task;

import java.util.Comparator;

public class TaskComparator implements Comparator<Task> {
    @Override
    public int compare(Task task1, Task task2) {
        // Use the priority field for comparison
        return Integer.compare(task2.getCumulativeTaskPriority(), task1.getCumulativeTaskPriority());
    }
}
