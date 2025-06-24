package com.drcita.user.models.appointmentbookingsummary;

public class CancelReason {
    private int id;
    private String reason;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return reason; // For Spinner to display this text
    }
}

