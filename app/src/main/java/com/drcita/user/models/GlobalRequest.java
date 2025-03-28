package com.drcita.user.models;

import com.google.gson.annotations.Expose;

public class GlobalRequest {
    @Expose
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
