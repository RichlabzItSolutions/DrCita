package com.drcita.user.models.departments;
import java.util.List;

public class DepartmentResponse {
    private boolean success;
    private String message;
    private List<Department> data;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<Department> getData() {
        return data;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(List<Department> data) {
        this.data = data;
    }
}
