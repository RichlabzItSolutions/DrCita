package com.drcita.user.models.profile;

public class DiseaseStatus {
    private int diseaseId;
    private int status;

    public DiseaseStatus(int diseaseId, int status) {
        this.diseaseId = diseaseId;
        this.status = status;
    }

    // Getters and Setters
    public int getDiseaseId() { return diseaseId; }
    public void setDiseaseId(int diseaseId) { this.diseaseId = diseaseId; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
}
