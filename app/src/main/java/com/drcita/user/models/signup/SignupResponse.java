
package com.drcita.user.models.signup;

import com.google.gson.annotations.Expose;

import java.util.List;


public class SignupResponse {

    @Expose
    private String status;
    @Expose
    private String message;
    @Expose
    private Data data;
    @Expose
    private List<String> errors;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}
