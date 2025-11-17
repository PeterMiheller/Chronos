package com.example.chronos.DTO;

import java.time.LocalDateTime;

public class EventResponse {
    private int id;
    private String type;
    private LocalDateTime eventDateTime;
    private String projectName;
    private int companyId;
    private String createdByName;

    public EventResponse() {
    }

    public EventResponse(int id, String type, LocalDateTime eventDateTime, String projectName, int companyId,
            String createdByName) {
        this.id = id;
        this.type = type;
        this.eventDateTime = eventDateTime;
        this.projectName = projectName;
        this.companyId = companyId;
        this.createdByName = createdByName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }
}
