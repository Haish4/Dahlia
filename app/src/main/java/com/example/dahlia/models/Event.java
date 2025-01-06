package com.example.dahlia.models;

public class Event {
    private String date;
    private String eventName;
    private String eventDetails;

    public Event(String date, String eventName, String eventDetails) {
        this.date = date;
        this.eventName = eventName;
        this.eventDetails = eventDetails;
    }

    public String getDate() { return date; }
    public String getEventName() { return eventName; }
    public String getEventDetails() { return eventDetails; }
}
