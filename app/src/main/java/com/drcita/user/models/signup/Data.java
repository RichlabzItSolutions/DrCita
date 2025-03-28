
package com.drcita.user.models.signup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Data {


    @SerializedName("userId")
    private String userId;
    @Expose
    private String name;
    @Expose
    private String otp;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

}
