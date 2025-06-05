package com.drcita.user.models.states;

public class StateRequest {
 private int  countryId;

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    @Override
    public String toString() {
        return "StateRequest{" +
                "countryId=" + countryId +
                '}';
    }
}
