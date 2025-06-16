package com.drcita.user.models.userprofile;

public class ProfileDiseaseResponse {

    private boolean success;
    private String message;
    private ProfileDiseaseData data;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public ProfileDiseaseData getData() { return data; }
}
