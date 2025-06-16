package com.drcita.user.models.appointment;

public class DocterDetailsRequest {

    private int providerId;
    private int doctorId;
    private String slotDate;

    public int getProviderId() {
        return providerId;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getSlotDate() {
        return slotDate;
    }

    public void setSlotDate(String slotDate) {
        this.slotDate = slotDate;
    }

    @Override
    public String toString() {
        return "DocterDetailsRequest{" +
                "providerId=" + providerId +
                ", doctorId=" + doctorId +
                ", slotDate='" + slotDate + '\'' +
                '}';
    }
}
