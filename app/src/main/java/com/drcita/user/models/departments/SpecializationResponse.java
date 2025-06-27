package com.drcita.user.models.departments;

import com.drcita.user.models.dashboard.specilization.Specialization;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SpecializationResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<Specialization> data;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<Specialization> getData() {
        return data;
    }
}
