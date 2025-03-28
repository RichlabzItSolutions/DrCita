
package com.drcita.user.models.forgotpassword;

import com.google.gson.annotations.Expose;


public class ForgotPasswordResponse {

    @Expose
    private Data data;
    @Expose
    private String message;
    @Expose
    private String status;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
