package com.example.chronos.DTO;

public class ClockInResponse {
    private Integer id;
    private Integer employeeId;
    private String clockInTime;
    private String clockOutTime;
    private String date;

    public ClockInResponse() {}

    public ClockInResponse(Integer id, Integer employeeId, String clockInTime, String clockOutTime, String date) {
        this.id = id;
        this.employeeId = employeeId;
        this.clockInTime = clockInTime;
        this.clockOutTime = clockOutTime;
        this.date = date;
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
}