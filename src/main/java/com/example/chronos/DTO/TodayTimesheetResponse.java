package com.example.chronos.DTO;

public class TodayTimesheetResponse {
    private Integer id;
    private Integer employeeId;
    private String clockInTime;
    private String clockOutTime;
    private String date;
    private Float hoursWorked;
    private Boolean isClockedIn;

    public TodayTimesheetResponse() {}

    public TodayTimesheetResponse(Integer id, Integer employeeId, String clockInTime, String clockOutTime, 
                                  String date, Float hoursWorked, Boolean isClockedIn) {
        this.id = id;
        this.employeeId = employeeId;
        this.clockInTime = clockInTime;
        this.clockOutTime = clockOutTime;
        this.date = date;
        this.hoursWorked = hoursWorked;
        this.isClockedIn = isClockedIn;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getEmployeeId() { return employeeId; }
    public void setEmployeeId(Integer employeeId) { this.employeeId = employeeId; }

    public String getClockInTime() { return clockInTime; }
    public void setClockInTime(String clockInTime) { this.clockInTime = clockInTime; }

    public String getClockOutTime() { return clockOutTime; }
    public void setClockOutTime(String clockOutTime) { this.clockOutTime = clockOutTime; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public Float getHoursWorked() { return hoursWorked; }
    public void setHoursWorked(Float hoursWorked) { this.hoursWorked = hoursWorked; }

    public Boolean getIsClockedIn() { return isClockedIn; }
    public void setIsClockedIn(Boolean isClockedIn) { this.isClockedIn = isClockedIn; }
}