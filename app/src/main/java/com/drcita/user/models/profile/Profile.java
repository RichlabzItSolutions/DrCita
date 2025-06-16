package com.drcita.user.models.profile;

public class Profile {
    private int subUserId;
    private int userId;
    private String fullName;
    private int gender;
    private int age;
    private String dob;
    private int maritalStatus;
    private String mobile;
    private String email;
    private int stateId;
    private int cityId;
    private String address;
    private int pastSurgeries;
    private int bloodGroupId;
    private String bloodGroup; // Nullable

    public int getSubUserId() {
        return subUserId;
    }

    public void setSubUserId(int subUserId) {
        this.subUserId = subUserId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public int getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(int maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPastSurgeries() {
        return pastSurgeries;
    }

    public void setPastSurgeries(int pastSurgeries) {
        this.pastSurgeries = pastSurgeries;
    }

    public int getBloodGroupId() {
        return bloodGroupId;
    }

    public void setBloodGroupId(int bloodGroupId) {
        this.bloodGroupId = bloodGroupId;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "subUserId=" + subUserId +
                ", userId=" + userId +
                ", fullName='" + fullName + '\'' +
                ", gender=" + gender +
                ", age=" + age +
                ", dob='" + dob + '\'' +
                ", maritalStatus=" + maritalStatus +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", stateId=" + stateId +
                ", cityId=" + cityId +
                ", address='" + address + '\'' +
                ", pastSurgeries=" + pastSurgeries +
                ", bloodGroupId=" + bloodGroupId +
                ", bloodGroup='" + bloodGroup + '\'' +
                '}';
    }
}
