
package com.drcita.user.models.otp;

import java.util.List;
import com.google.gson.annotations.Expose;


public class VerifyotpResponse {

    @Expose
    private Object data;
    @Expose
    private List<String> errors;
    @Expose
    private String status;
    @Expose
    private String message;


    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
