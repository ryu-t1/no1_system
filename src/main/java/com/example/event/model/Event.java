package com.example.event.model;

public class Event {
    private int id;
    private String title;
    private String description;
    private String eventDate; 
    private int capacity;

    public Event() {}

    public Event(int id, String title, String description, String eventDate, int capacity) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.eventDate = eventDate;
        this.capacity = capacity;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getEventDate() { return eventDate; }
    public int getCapacity() { return capacity; }

    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setEventDate(String eventDate) { this.eventDate = eventDate; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", eventDate='" + eventDate + '\'' +
                ", capacity=" + capacity +
                '}';
    }
}
