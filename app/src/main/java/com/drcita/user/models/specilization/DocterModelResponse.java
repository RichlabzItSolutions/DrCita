package com.drcita.user.models.specilization;

import java.util.List;

public class DocterModelResponse {
private boolean success;
private String message;
private List<DoctorModel> data;

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

    public List<DoctorModel> getData() {
        return data;
    }

    public void setData(List<DoctorModel> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DocterModelResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
