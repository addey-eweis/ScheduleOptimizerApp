package com.example.optimizedschedule.taskListHandeling;

// Task.java
public class Task {
    private String id;
    private String taskName;
    private String taskDueDate;
    private String taskTimeHours;
    private String taskTimeMinutes;
    private String taskPriority;
    int timeConsumed;

    public int getTimeConsumed() {
        return timeConsumed;
    }

    public void setTimeConsumed(int timeConsumed) {
        this.timeConsumed = timeConsumed;
    }

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

    public Task(String taskName, String taskTimeHours, String taskTimeMinutes){
        this.taskName = taskName;
        this.taskTimeHours = taskTimeHours;
        this.taskTimeMinutes = taskTimeMinutes;
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
