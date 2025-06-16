package com.drcita.user.models.appointment;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;


// Top-level response

@Parcel
public class AppointmentResponse {

    public boolean success;

    public String message;

    public AppointmentData data;

    // Getters & Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public AppointmentData getData() { return data; }
    public void setData(AppointmentData data) { this.data = data; }
}
