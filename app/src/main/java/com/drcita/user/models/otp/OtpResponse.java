package com.drcita.user.models.otp;

public class OtpResponse {
    private boolean success;
    private String message;
    private VerifyOtPData data;


    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public VerifyOtPData getData() {
        return data;
    }
    public void setData(VerifyOtPData data) {
        this.data = data;
    }
}
