package com.drcita.user.models.appointment;

public class ResechudleAppointmentRequest {
    private int id;
    private String appointmentDate;
    private String slotTime;
    private int appointmentMode;
    private String userId;

    public ResechudleAppointmentRequest(int id, String appointmentDate, String slotTime, int appointmentMode, String userId) {
        this.id = id;
        this.appointmentDate = appointmentDate;
        this.slotTime = slotTime;
        this.appointmentMode = appointmentMode;
        this.userId = userId;
    }
}
