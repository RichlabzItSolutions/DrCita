package com.drcita.user.models.newProviderlist;

import java.util.List;

public class ProvidersRequestData {
    private int cityId;
    private String searchStr;
    private List<Integer> specializationId;
    private List<Integer> consultationMode;
    private String area;

    // Getters and Setters
    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getSearchStr() {
        return searchStr;
    }

    public void setSearchStr(String searchStr) {
        this.searchStr = searchStr;
    }

    public List<Integer> getSpecializationId() {
        return specializationId;
    }

    public void setSpecializationId(List<Integer> specializationId) {
        this.specializationId = specializationId;
    }

    public List<Integer> getConsultationMode() {
        return consultationMode;
    }

    public void setConsultationMode(List<Integer> consultationMode) {
        this.consultationMode = consultationMode;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }


}
