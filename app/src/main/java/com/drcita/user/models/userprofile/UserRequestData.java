package com.drcita.user.models.userprofile;

public class UserRequestData {

    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserRequestData{" +
                "userId='" + userId + '\'' +
                '}';
    }
}
