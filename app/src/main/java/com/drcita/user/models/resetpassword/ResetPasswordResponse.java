
package com.drcita.user.models.resetpassword;

import com.google.gson.annotations.Expose;


public class ResetPasswordResponse {

    @Expose
    private Object data;
    @Expose
    private String message;
    @Expose
    private String status;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
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
