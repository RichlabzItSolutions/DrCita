package com.drcita.user.models.cities;

public class CityRequestData {
    private int stateId;

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    @Override
    public String toString() {
        return "CityRequestData{" +
                "stateId=" + stateId +
                '}';
    }
}
