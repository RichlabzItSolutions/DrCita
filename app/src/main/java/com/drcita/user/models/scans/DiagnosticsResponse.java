package com.drcita.user.models.scans;

import java.util.List;

public class DiagnosticsResponse {
    private List<com.drcita.user.models.hospitals.DataItem> data;
    private String message;
    private String status;

    public void setData(List<com.drcita.user.models.hospitals.DataItem> data) {
        this.data = data;
    }

    public List<com.drcita.user.models.hospitals.DataItem> getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}