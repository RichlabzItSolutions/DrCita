package com.drcita.user.models.appointmentbookingsummary;

public class CancelRequest {

    private int id;
    private String userId;
    private String reason;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public CancelRequest(int id, String userId, String reason) {
        this.id = id;
        this.userId = userId;
        this.reason = reason;
    }
}
