
package com.drcita.user.models.forgotpassword;

import com.google.gson.annotations.Expose;


public class Data {

    @Expose
    private Long otp;

    public Long getOtp() {
        return otp;
    }

    public void setOtp(Long otp) {
        this.otp = otp;
    }

}
