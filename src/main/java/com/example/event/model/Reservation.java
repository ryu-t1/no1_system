package com.example.event.model;

public class Reservation {
    private int id;
    private int eventId;
    private String name;
    private String email;
    private String reservedAt;
    private int userId; // ← 追加！

    // Getter & Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getReservedAt() { return reservedAt; }
    public void setReservedAt(String reservedAt) { this.reservedAt = reservedAt; }

    public int getUserId() { return userId; }        // ← 追加！
    public void setUserId(int userId) { this.userId = userId; }  // ← 追加！
}
