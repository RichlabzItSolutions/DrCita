package com.drcita.user.models.cities;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CityResponse {

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private List<City> data;

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

    public List<City> getData() {
        return data;
    }

    public void setData(List<City> data) {
        this.data = data;
    }

    public static class City {

        @SerializedName("id")
        @Expose
        private int id;

        @SerializedName("stateName")
        @Expose
        private String stateName;

        @SerializedName("cityName")
        @Expose
        private String cityName;

        @SerializedName("status")
        @Expose
        private int status;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getStateName() {
            return stateName;
        }

        public void setStateName(String stateName) {
            this.stateName = stateName;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
