package com.drcita.user.models.appointmentbookingsummary;

import java.util.List;

public class CancelReasonResponse {
    private boolean success;
    private String message;
    private List<CancelReason> data;

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CancelReason> getData() {
        return data;
    }

    public void setData(List<CancelReason> data) {
        this.data = data;
    }
}
