package com.drcita.user.models.userprofile;

public class Member {
    private int subUserId;
    private int userId;
    private String fullName;
    private int relationId;
    private String relation;

    // Getters and Setters
    public int getSubUserId() { return subUserId; }
    public void setSubUserId(int subUserId) { this.subUserId = subUserId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public int getRelationId() { return relationId; }
    public void setRelationId(int relationId) { this.relationId = relationId; }

    public String getRelation() { return relation; }
    public void setRelation(String relation) { this.relation = relation; }
}

