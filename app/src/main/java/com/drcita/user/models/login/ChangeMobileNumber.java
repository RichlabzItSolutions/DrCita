package com.drcita.user.models.login;

public class ChangeMobileNumber {
    private int userId;
    public String mobile;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "ChangeMobileNumber{" +
                "userId=" + userId +
                ", mobile='" + mobile + '\'' +
                '}';
    }
}

