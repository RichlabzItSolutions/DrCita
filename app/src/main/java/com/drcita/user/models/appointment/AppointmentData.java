package com.drcita.user.models.appointment;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class AppointmentData {

    public int appointmentId;
    public int appointmentNum;
    public String slotTime;

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getAppointmentNum() {
        return appointmentNum;
    }

    public void setAppointmentNum(int appointmentNum) {
        this.appointmentNum = appointmentNum;
    }

    public String getSlotTime() {
        return slotTime;
    }

    public void setSlotTime(String slotTime) {
        this.slotTime = slotTime;
    }

    @Override
    public String toString() {
        return "AppointmentData{" +
                "appointmentId=" + appointmentId +
                ", appointmentNum=" + appointmentNum +
                ", slotTime='" + slotTime + '\'' +
                '}';
    }
}
