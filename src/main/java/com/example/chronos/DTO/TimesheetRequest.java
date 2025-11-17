package com.example.chronos.DTO;

public class TimesheetRequest {
    private String date; // ISO date YYYY-MM-DD
    private float hours;

    public TimesheetRequest() {
    }

    public TimesheetRequest(String date, float hours) {
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
