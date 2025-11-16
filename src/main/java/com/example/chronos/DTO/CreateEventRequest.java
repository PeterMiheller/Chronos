package com.example.chronos.DTO;

import java.time.LocalDateTime;

public class CreateEventRequest {
    private String type; // "TEAM_MEETING" or "PROJECT_DEADLINE"
    private LocalDateTime eventDateTime;
    private String projectName; // Optional, only for PROJECT_DEADLINE

    public CreateEventRequest() {
    }

    public CreateEventRequest(String type, LocalDateTime eventDateTime, String projectName) {
        this.type = type;
        this.eventDateTime = eventDateTime;
        this.projectName = projectName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getEventDateTime() {
        return eventDateTime;
    }

    public void setEventDateTime(LocalDateTime eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
