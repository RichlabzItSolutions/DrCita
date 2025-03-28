package com.drcita.user.models.region;

public class UpdateRegionRequest {
    int userId;
    int region;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRegion() {
        return region;
    }

    public void setRegion(int region) {
        this.region = region;
    }

    @Override
    public String toString() {
        return "UpdateRegionRequest{" +
                "userId=" + userId +
                ", region=" + region +
                '}';
    }
}
