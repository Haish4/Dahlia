package com.example.dahlia.models;

import java.io.Serializable;

public class Petition {
    private String topic;
    private String details;
    private int minSigned, count;
    private String dateEnds;
    private String type;

    public Petition(String topic, String details, int minSigned, String dateEnds, String type, int count
    ) {
        this.topic = topic;
        this.details = details;
        this.minSigned = minSigned;
        this.dateEnds = dateEnds;
        this.type = type;
        this.count = count;
    }

    public Petition(String topic, String type) {
        this.topic = topic;
        this.type = type;
    }

    public String getTopic() {
        return topic;
    }

    public String getDetails() {
        return details;
    }

    public int getMinSigned() {
        return minSigned;
    }

    public String getDateEnds() {
        return dateEnds;
    }

    public String getType() {
        return type;
    }

    public int getCount() { return count; }
}

