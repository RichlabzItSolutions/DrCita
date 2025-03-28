package com.drcita.user.models.medicalrecords;

public class GetMedicalRecordsRequest {
    private int userId;

    public void setUserId(int userId){
        this.userId = userId;
    }

    public int getUserId(){
        return userId;
    }
}
