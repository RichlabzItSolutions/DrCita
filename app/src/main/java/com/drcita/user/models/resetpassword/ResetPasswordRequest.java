
package com.drcita.user.models.resetpassword;

import com.google.gson.annotations.Expose;

public class ResetPasswordRequest {

    @Expose
    private String confirmPassword;
    @Expose
    private String mobile;
    @Expose
    private String password;

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
