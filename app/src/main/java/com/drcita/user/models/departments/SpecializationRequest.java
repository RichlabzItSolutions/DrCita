package com.drcita.user.models.departments;

public class SpecializationRequest {
    private String departmentId;

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public SpecializationRequest(String departmentId) {
        this.departmentId = departmentId;
    }
}
