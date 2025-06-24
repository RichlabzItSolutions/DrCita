package com.drcita.user.models.medicalrecords;

public class MedicalRecordData {
    private int id;
    private String title;
    private String description;
    private String addedOn;
    private int userId;
    private String userName;
    private MedicalRecordAssets assets;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public MedicalRecordAssets getAssets() {
        return assets;
    }
}
