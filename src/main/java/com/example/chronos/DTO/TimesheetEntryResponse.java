package com.example.chronos.DTO;

public class TimesheetEntryResponse {
    private String date; // ISO yyyy-MM-dd
    private float hours;

    public TimesheetEntryResponse() {
    }

    public TimesheetEntryResponse(String date, float hours) {
        this.date = date;
        this.hours = hours;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getHours() {
        return hours;
    }

    public void setHours(float hours) {
        this.hours = hours;
    }
}
