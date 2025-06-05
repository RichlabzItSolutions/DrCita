
package com.drcita.user.models.login;

import com.google.gson.annotations.Expose;


public class LoginRequest {

    @Expose
    private String mobile;
//    @Expose
//    private String password;
//    @Expose
//    private String deviceToken;
//
//    public String getDeviceToken() {
//        return deviceToken;
//    }
//
//    public void setDeviceToken(String deviceToken) {
//        this.deviceToken = deviceToken;
//    }
//
//    public String getMobile() {
//        return mobile;
//    }
//
//    public void setMobile(String mobile) {
//        this.mobile = mobile;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "mobile='" + mobile + '\'' +
                '}';
    }
}
