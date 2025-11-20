package com.example.chronos.DTO;

import com.example.chronos.model.User;

public class DashboardSummaryResponse {
    private User user;
    private VacationDaysSummary vacationDaysSummary;
    private WeeklyHoursSummary weeklyHours;
    private Integer pendingRequests;

    public static class VacationDaysSummary {
        private Integer total;
        private Integer used;
        private Integer remaining;

        public VacationDaysSummary() {}

        public VacationDaysSummary(Integer total, Integer used, Integer remaining) {
            this.total = total;
            this.used = used;
            this.remaining = remaining;
        }

        public Integer getTotal() { return total; }
        public void setTotal(Integer total) { this.total = total; }

        public Integer getUsed() { return used; }
        public void setUsed(Integer used) { this.used = used; }

        public Integer getRemaining() { return remaining; }
        public void setRemaining(Integer remaining) { this.remaining = remaining; }
    }

    public static class WeeklyHoursSummary {
        private Float hoursThisWeek;
        private Float targetHours;

        public WeeklyHoursSummary() {}

        public WeeklyHoursSummary(Float hoursThisWeek, Float targetHours) {
            this.hoursThisWeek = hoursThisWeek;
            this.targetHours = targetHours;
        }

        public Float getHoursThisWeek() { return hoursThisWeek; }
        public void setHoursThisWeek(Float hoursThisWeek) { this.hoursThisWeek = hoursThisWeek; }

        public Float getTargetHours() { return targetHours; }
        public void setTargetHours(Float targetHours) { this.targetHours = targetHours; }
    }

    public DashboardSummaryResponse() {}

    public DashboardSummaryResponse(User user, VacationDaysSummary vacationDaysSummary, WeeklyHoursSummary weeklyHours, Integer pendingRequests) {
        this.user = user;
        this.vacationDaysSummary = vacationDaysSummary;
        this.weeklyHours = weeklyHours;
        this.pendingRequests = pendingRequests;
    }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public VacationDaysSummary getVacationDaysSummary() { return vacationDaysSummary; }
    public void setVacationDaysSummary(VacationDaysSummary vacationDaysSummary) { this.vacationDaysSummary = vacationDaysSummary; }

    public WeeklyHoursSummary getWeeklyHours() { return weeklyHours; }
    public void setWeeklyHours(WeeklyHoursSummary weeklyHours) { this.weeklyHours = weeklyHours; }

    public Integer getPendingRequests() { return pendingRequests; }
    public void setPendingRequests(Integer pendingRequests) { this.pendingRequests = pendingRequests; }
}