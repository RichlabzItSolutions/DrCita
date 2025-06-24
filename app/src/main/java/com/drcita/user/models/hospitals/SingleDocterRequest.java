package com.drcita.user.models.hospitals;

public class SingleDocterRequest {
    private int doctorId;
    private int providerId;

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getProviderId() {
        return providerId;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

    @Override
    public String toString() {
        return "SingleDocterRequest{" +
                "doctorId=" + doctorId +
                ", providerId=" + providerId +
                '}';
    }
}
