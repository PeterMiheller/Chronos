package com.example.chronos.DTO;

public class WeeklyHoursResponse {
    private String date;
    private Float hoursWorked;
    private Boolean isClockedIn;

    public WeeklyHoursResponse() {}

    public WeeklyHoursResponse(String date, Float hoursWorked, Boolean isClockedIn) {
        this.date = date;
        this.hoursWorked = hoursWorked;
        this.isClockedIn = isClockedIn;
    }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public Float getHoursWorked() { return hoursWorked; }
    public void setHoursWorked(Float hoursWorked) { this.hoursWorked = hoursWorked; }

    public Boolean getIsClockedIn() { return isClockedIn; }
    public void setIsClockedIn(Boolean isClockedIn) { this.isClockedIn = isClockedIn; }
}