package com.drcita.user.models.home;

public class HomeDataRequest {
 private int stateId=24;
 private int cityId=14;


    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    @Override
    public String toString() {
        return "HomeDataRequest{" +
                "stateId=" + stateId +
                ", cityId=" + cityId +
                '}';
    }
}
