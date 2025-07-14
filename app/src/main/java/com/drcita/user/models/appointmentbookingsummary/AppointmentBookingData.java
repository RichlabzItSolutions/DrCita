package com.drcita.user.models.appointmentbookingsummary;

import java.util.List;

public class AppointmentBookingData {
    private int appointmentId;
    private String appointmentNum;
    private String fee;
    private String doctorName;
    private String scanName;
    private String hospitalName;
    private String area;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    private String paidAmount;

    private List<String> specializationName;
    private String picture;
    private String slotTime;
    private String slotDate;
    private int appointmentMode;   // e.g., 1 = Offline, 2 = Online
    private int appointmentType;   // Additional type if needed
    private int appointmentStatus; // 1-New, 2-Confirmed, 3-Completed, 4-Cancelled, 5-Refunded
    private String hospitalType;

    public AppointmentBookingData(int appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    // Getters and Setters
    public int getAppointmentId() {
        return appointmentId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getScanName() {
        return scanName;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public void setScanName(String scanName) {
        this.scanName = scanName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public void setSpecializationName(List<String> specializationName) {
        this.specializationName = specializationName;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setSlotTime(String slotTime) {
        this.slotTime = slotTime;
    }

    public void setSlotDate(String slotDate) {
        this.slotDate = slotDate;
    }

    public void setAppointmentMode(int appointmentMode) {
        this.appointmentMode = appointmentMode;
    }

    public void setAppointmentType(int appointmentType) {
        this.appointmentType = appointmentType;
    }

    public void setAppointmentStatus(int appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public void setHospitalType(String hospitalType) {
        this.hospitalType = hospitalType;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public List<String> getSpecializationName() {
        return specializationName;
    }

    public String getPicture() {
        return picture;
    }

    public String getSlotTime() {
        return slotTime;
    }

    public String getSlotDate() {
        return slotDate;
    }

    public int getAppointmentMode() {
        return appointmentMode;
    }

    public int getAppointmentType() {
        return appointmentType;
    }

    public int getAppointmentStatus() {
        return appointmentStatus;
    }

    public String getHospitalType() {
        return hospitalType;
    }

    public String getAppointmentNum() {
        return appointmentNum;
    }

    public void setAppointmentNum(String appointmentNum) {
        this.appointmentNum = appointmentNum;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }

    // Optional: Convert appointmentStatus to readable string
    public String getStatusString() {
        switch (appointmentStatus) {
            case 1:
                return "New";
            case 2:
                return "Confirmed";
            case 3:
                return "Completed";
            case 4:
                return "Cancelled";
            case 5:
                return "Refunded";
            default:
                return "Unknown";
        }
    }

    public boolean isOnline() {
        return appointmentMode == 2;
    }

}
