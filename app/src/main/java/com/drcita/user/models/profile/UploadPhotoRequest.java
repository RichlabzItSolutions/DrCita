package com.drcita.user.models.profile;

import com.google.gson.annotations.SerializedName;

public class UploadPhotoRequest {
    @SerializedName("userId")
    private String userId;
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
