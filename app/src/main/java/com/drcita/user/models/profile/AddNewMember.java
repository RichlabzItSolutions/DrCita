package com.drcita.user.models.profile;

import java.util.List;

public class AddNewMember {
    private int userId;
    private String fullName;
    private int gender;
    private int age;
    private String dob;
    private String maritalStatus;
    private String mobile;
    private String email;
    private String stateId;
    private String cityId;
    private String address;
    private String pastSurgeries;
    private String bloodGroupId;
    private int relationId;
    private List<Disease> diseases;

    // Inner Disease class
    public static class Disease {
        private int diseaseId;
        private int status;

        public int getDiseaseId() {
            return diseaseId;
        }

        public void setDiseaseId(int diseaseId) {
            this.diseaseId = diseaseId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }

    // Getters and Setters

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

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
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

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPastSurgeries() {
        return pastSurgeries;
    }

    public void setPastSurgeries(String pastSurgeries) {
        this.pastSurgeries = pastSurgeries;
    }

    public String getBloodGroupId() {
        return bloodGroupId;
    }

    public void setBloodGroupId(String bloodGroupId) {
        this.bloodGroupId = bloodGroupId;
    }

    public int getRelationId() {
        return relationId;
    }

    public void setRelationId(int relationId) {
        this.relationId = relationId;
    }

    public List<Disease> getDiseases() {
        return diseases;
    }

    public void setDiseases(List<Disease> diseases) {
        this.diseases = diseases;
    }

    public AddNewMember(int userId, String fullName, int gender, int age, String dob, String maritalStatus, String mobile, String email, String stateId, String cityId, String address, String pastSurgeries,
                        String bloodGroupId, int relationId, List<Disease> diseases) {
        this.userId = userId;
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
        this.diseases = diseases;
    }
}
