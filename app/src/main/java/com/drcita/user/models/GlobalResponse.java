package com.drcita.user.models;

import com.drcita.user.models.profile.Data;

public class GlobalResponse {
    private Data data;
    private String message;
    private String status;

    public void setData(Data data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Data getData(){
        return data;
    }

    public String getMessage(){
        return message;
    }

    public String getStatus(){
        return status;
    }
}
