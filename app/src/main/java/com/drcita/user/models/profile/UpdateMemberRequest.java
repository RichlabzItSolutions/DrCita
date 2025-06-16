package com.drcita.user.models.profile;

import java.util.List;

public class UpdateMemberRequest {

    private int subUserId;
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
    private int relationId;
    private int foodType;
    private List<DiseaseStatus> diseases;

    // Getters and Setters

    public int getSubUserId() {
        return subUserId;
    }

    public void setSubUserId(int subUserId) {
        this.subUserId = subUserId;
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

    public int getRelationId() {
        return relationId;
    }

    public void setRelationId(int relationId) {
        this.relationId = relationId;
    }

    public int getFoodType() {
        return foodType;
    }

    public void setFoodType(int foodType) {
        this.foodType = foodType;
    }

    public List<DiseaseStatus> getDiseases() {
        return diseases;
    }

    public void setDiseases(List<DiseaseStatus> diseases) {
        this.diseases = diseases;
    }

    public UpdateMemberRequest(int subUserId, String fullName, int gender, int age, String dob, int maritalStatus, String mobile, String email, int stateId, int cityId, String address, int pastSurgeries, int bloodGroupId,
                               int relationId, int foodType, List<DiseaseStatus> diseases) {
        this.subUserId = subUserId;
        this.fullName = fullName;
        this.gender = gender;
        this.age = age;
        this.dob = dob;
        this.maritalStatus = maritalStatus;
        this.mobile = mobile;
        this.email = email;
        this.stateId = stateId;
        this.cityId = cityId;
        this.address = address;
        this.pastSurgeries = pastSurgeries;
        this.bloodGroupId = bloodGroupId;
        this.relationId = relationId;
        this.foodType = foodType;
        this.diseases = diseases;
    }
}
