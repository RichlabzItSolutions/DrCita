package com.drcita.user.models.profile;

public class CheckUserRequest {

    private int subUserId;

    public int getSubUserId() {
        return subUserId;
    }

    public void setSubUserId(int subUserId) {
        this.subUserId = subUserId;
    }

    @Override
    public String toString() {
        return "CheckUserRequest{" +
                "subUserId=" + subUserId +
                '}';
    }
}
