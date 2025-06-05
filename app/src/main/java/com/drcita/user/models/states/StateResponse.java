package com.drcita.user.models.states;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StateResponse {

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private List<State> data;

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

    public List<State> getData() {
        return data;
    }

    public void setData(List<State> data) {
        this.data = data;
    }

    public static class State {

        @SerializedName("id")
        @Expose
        private int id;

        @SerializedName("countryName")
        @Expose
        private String countryName;

        @SerializedName("stateName")
        @Expose
        private String stateName;

        @SerializedName("status")
        @Expose
        private int status;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public String getStateName() {
            return stateName;
        }

        public void setStateName(String stateName) {
            this.stateName = stateName;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}

