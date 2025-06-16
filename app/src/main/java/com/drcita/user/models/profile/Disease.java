package com.drcita.user.models.profile;

public class Disease {
    private int id;
    private String diseaseName;
    private String disease;
    private int status;
    private int selection; // 0 = none, 1 = yes, 2 = no

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    // Constructor
    public Disease(int id, String diseaseName, int status) {
        this.id = id;
        this.diseaseName = diseaseName;
        this.status = status;
        this.selection = status; // Automatically assign status to selection
    }

    public int getSelection() {
        return selection;
    }

    public void setSelection(int selection) {
        this.selection = selection;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Disease{" +
                "id=" + id +
                ", diseaseName='" + diseaseName + '\'' +
                ", disease='" + disease + '\'' +
                ", status=" + status +
                ", selection=" + selection +
                '}';
    }
}
