package com.drcita.user.models.profile;

import java.util.List;

public class BloodGroupResponse {
    private boolean success;
    private String message;
    private List<BloodGroup> data;

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

    public List<BloodGroup> getData() {
        return data;
    }

    public void setData(List<BloodGroup> data) {
        this.data = data;
    }
}

