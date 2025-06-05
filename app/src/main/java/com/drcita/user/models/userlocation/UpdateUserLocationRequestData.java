package com.drcita.user.models.userlocation;

public class UpdateUserLocationRequestData {
    private String userId;
    private int stateId;
    private int cityId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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
        return "UpdateUserLocationRequestData{" +
                "userId='" + userId + '\'' +
                ", stateId=" + stateId +
                ", cityId=" + cityId +
                '}';
    }
}
