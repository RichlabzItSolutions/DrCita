package com.drcita.user.models.region;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CustomRegionData {
    private String regionhead;
    @SerializedName("data")
    private List<DataItem> data;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private String status;

    public String getRegionhead() {
        return regionhead;
    }

    public void setRegionhead(String regionhead) {
        this.regionhead = regionhead;
    }

    public List<DataItem> getData() {
        return data;
    }

    public void setData(List<DataItem> data) {
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

    @Override
    public String toString() {
        return "CustomNotificationData{" +
                "regionhead='" + regionhead + '\'' +
                ", data=" + data +
                ", message='" + message + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
