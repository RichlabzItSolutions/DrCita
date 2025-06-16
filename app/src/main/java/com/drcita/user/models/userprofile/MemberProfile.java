package com.drcita.user.models.userprofile;

public class MemberProfile {
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
    private String bloodGroup;
    private int relationId;
    private String relation;
    private int foodType;

    public int getSubUserId() { return subUserId; }
    public int getUserId() { return userId; }
    public String getFullName() { return fullName; }
    public int getGender() { return gender; }
    public int getAge() { return age; }
    public String getDob() { return dob; }
    public int getMaritalStatus() { return maritalStatus; }
    public String getMobile() { return mobile; }
    public String getEmail() { return email; }
    public int getStateId() { return stateId; }
    public int getCityId() { return cityId; }
    public String getAddress() { return address; }
    public int getPastSurgeries() { return pastSurgeries; }
    public int getBloodGroupId() { return bloodGroupId; }
    public String getBloodGroup() { return bloodGroup; }
    public int getRelationId() { return relationId; }
    public String getRelation() { return relation; }
    public int getFoodType() { return foodType; }
}
