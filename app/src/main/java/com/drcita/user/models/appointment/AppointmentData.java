package com.drcita.user.models.appointment;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class AppointmentData {

    public int appointmentId;


    public String slotTime;

    // Constructor

    // Getters & Setters
    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }

    public String getSlotTime() { return slotTime; }
    public void setSlotTime(String slotTime) { this.slotTime = slotTime; }
}
