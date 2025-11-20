package com.example.chronos.DTO;

import com.example.chronos.model.VacationStatus;

public class VacationStatusUpdateRequest {
    private VacationStatus status;

    public VacationStatusUpdateRequest() {
    }
    public VacationStatus getStatus() {
        return status;
    }

    public void setStatus(VacationStatus status) {
        this.status = status;
    }
}
