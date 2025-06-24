package com.drcita.user.models.newProviderlist;

public class NewProviderList {
    private int providerId;
    private String hospitalName;
    private String mobile;
    private String address;
    private String picture;
    private int consultationMode;
    private String hospitalType;
    private String area;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    // Getters and setters
    public int getProviderId() { return providerId; }
    public void setProviderId(int providerId) { this.providerId = providerId; }

    public String getHospitalName() { return hospitalName; }
    public void setHospitalName(String hospitalName) { this.hospitalName = hospitalName; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPicture() { return picture; }
    public void setPicture(String picture) { this.picture = picture; }

    public int getConsultationMode() { return consultationMode; }
    public void setConsultationMode(int consultationMode) { this.consultationMode = consultationMode; }

    public String getHospitalType() { return hospitalType; }
    public void setHospitalType(String hospitalType) { this.hospitalType = hospitalType; }
}

