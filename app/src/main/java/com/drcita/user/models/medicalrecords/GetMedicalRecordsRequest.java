package com.drcita.user.models.medicalrecords;

public class GetMedicalRecordsRequest {
    private int subUserId;
    private String appointmentId;


    public int getSubUserId() {
        return subUserId;
    }

    public void setSubUserId(int subUserId) {
        this.subUserId = subUserId;


    }
    public String getAppointmentId() {
        return appointmentId;
    }
    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }
}
