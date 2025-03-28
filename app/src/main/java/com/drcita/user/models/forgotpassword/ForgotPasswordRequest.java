
package com.drcita.user.models.forgotpassword;

import com.google.gson.annotations.Expose;


public class ForgotPasswordRequest {

    @Expose
    private String mobile;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

}
