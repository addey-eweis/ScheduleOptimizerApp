package com.example.optimizedschedule.activities;

public class ScheduleItem {
    public enum ItemType {
        TASK, BREAK
    }

    private ItemType type;
    private String title; // Task name or break type (e.g., "Lunch Break")
    private String itemTimeTaken;
    private String timeSlot; // E.g., "09:00 AM - 10:00 AM"

    // Constructor
    public ScheduleItem(ItemType type, String title, String itemTimeTaken, String timeSlot) {
        this.type = type;
        this.title = title;
        this.itemTimeTaken = itemTimeTaken;
        this.timeSlot = timeSlot;
    }

    // Getters and Setters
    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getItemTimeTaken() {
        return itemTimeTaken;
    }

    public void setItemTimeTaken(String itemTimeTaken) {
        this.itemTimeTaken = itemTimeTaken;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

}
