package com.example.optimizedschedule.taskListHandeling;

// Task.java
public class Task {
    private String id;
    private String taskName;
    private String taskDueDate;
    private String taskTimeHours;
    private String taskTimeMinutes;
    private String taskPriority;

    public int getCumulativeTaskPriority() {
        return cumulativeTaskPriority;
    }

    public void setCumulativeTaskPriority(int cumulativeTaskPriority) {
        this.cumulativeTaskPriority = cumulativeTaskPriority;
    }

    private int cumulativeTaskPriority;

    public Task(String id, String taskName, String taskDueDate, String taskTimeHours, String taskTimeMinutes, String taskPriority) {
        this.id = id;
        this.taskName = taskName;
        this.taskDueDate = taskDueDate;
        this.taskTimeHours = taskTimeHours;
        this.taskTimeMinutes = taskTimeMinutes;
        this.taskPriority = taskPriority;
//        this.CumulativeTaskPriority = CumulativeTaskPriority;
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
