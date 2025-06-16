package com.drcita.user.models.userprofile;

import java.util.List;

public class UsersResponse {
    private boolean success;
    private String message;
    private List<UserData> data;

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
    public List<UserData> getData() {
        return data;
    }
    public void setData(List<UserData> data) {
        this.data = data;
    }


}
