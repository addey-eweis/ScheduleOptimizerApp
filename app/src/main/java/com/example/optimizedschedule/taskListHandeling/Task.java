package com.example.optimizedschedule.taskListHandeling;

import android.graphics.drawable.Drawable;
import android.widget.Button;

// Task.java
public class Task {
    private String id;
    private String taskName;
    private String taskDueDate;
    private String taskTimeHours;
    private String taskTimeMinutes;
    private String taskPriority;

    public Task(String id, String taskName, String taskDueDate, String taskTimeHours, String taskTimeMinutes, String taskPriority) {
        this.id = id;
        this.taskName = taskName;
        this.taskDueDate = taskDueDate;
        this.taskTimeHours = taskTimeHours;
        this.taskTimeMinutes = taskTimeMinutes;
        this.taskPriority = taskPriority;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskDueDate() {
        return taskDueDate;
    }

    public String getTaskTimeHours() {
        return taskTimeHours;
    }

    public String getTaskTimeMinutes() {
        return taskTimeMinutes;
    }

    public String getTaskPriority() {
        return taskPriority;
    }

    public String getId() {
        return id;
    }

}
