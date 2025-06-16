package com.drcita.user.models.userprofile;

public class MemeberProfileRequest {

    private int subUserId;

    public int getSubUserId() {
        return subUserId;
    }

    public void setSubUserId(int subUserId) {
        this.subUserId = subUserId;
    }

    @Override
    public String toString() {
        return "MemeberProfileRequest{" +
                "subUserId=" + subUserId +
                '}';
    }
}
