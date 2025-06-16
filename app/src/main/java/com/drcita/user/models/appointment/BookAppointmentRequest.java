package com.drcita.user.models.appointment;

import com.google.gson.annotations.SerializedName;

public class BookAppointmentRequest {

    @SerializedName("doctorId")
    private int doctorId;

    @SerializedName("providerId")
    private int providerId;

    @SerializedName("appointmentDate")
    private String appointmentDate;   // format: "dd-MM-yyyy"

    @SerializedName("slotTime")
    private String slotTime;          // e.g. "08:00 AM"

    @SerializedName("userId")
    private int userId;

    @SerializedName("subUserId")
    private int subUserId;

    @SerializedName("appointmentMode")
    private int appointmentMode;
    // e.g. 1 = online, 2 = offline
    @SerializedName("appointmentType")
    private String appointmentType;

    @SerializedName("consultationFee")
    private int consultationFee;

    @SerializedName("couponId")
    private int couponId;
    @SerializedName("couponDiscount")
    private int couponDiscount;
    @SerializedName("charges")
    private Integer charges;

    @SerializedName("totalAmount")
    private Integer totalAmount;

    @SerializedName("transactionId")
    private String transactionId;

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

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getSlotTime() {
        return slotTime;
    }

    public void setSlotTime(String slotTime) {
        this.slotTime = slotTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSubUserId() {
        return subUserId;
    }

    public void setSubUserId(int subUserId) {
        this.subUserId = subUserId;
    }

    public int getAppointmentMode() {
        return appointmentMode;
    }


    public BookAppointmentRequest(int doctorId, int providerId, String appointmentDate, String slotTime, int userId,
                                  int subUserId, int appointmentMode, String appointmentType,
                                  int consultationFee, int couponId, int couponDiscount, Integer charges,
                                  Integer totalAmount, String transactionId) {
        this.doctorId = doctorId;
        this.providerId = providerId;
        this.appointmentDate = appointmentDate;
        this.slotTime = slotTime;
        this.userId = userId;
        this.subUserId = subUserId;
        this.appointmentMode = appointmentMode;
        this.appointmentType = appointmentType;
        this.consultationFee = consultationFee;
        this.couponId = couponId;
        this.couponDiscount = couponDiscount;
        this.charges = charges;
        this.totalAmount = totalAmount;
        this.transactionId = transactionId;
    }

    public void setAppointmentMode(int appointmentMode) {
        this.appointmentMode = appointmentMode;
    }

    public int getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(int consultationFee) {
        this.consultationFee = consultationFee;
    }

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }

    public int getCouponDiscount() {
        return couponDiscount;
    }

    public void setCouponDiscount(int couponDiscount) {
        this.couponDiscount = couponDiscount;
    }

    public Integer getCharges() {
        return charges;
    }

    public void setCharges(Integer charges) {
        this.charges = charges;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        return "BookAppointmentRequest{" +
                "doctorId=" + doctorId +
                ", providerId=" + providerId +
                ", appointmentDate='" + appointmentDate + '\'' +
                ", slotTime='" + slotTime + '\'' +
                ", userId=" + userId +
                ", subUserId=" + subUserId +
                ", appointmentMode=" + appointmentMode +
                ", consultationFee=" + consultationFee +
                ", couponId=" + couponId +
                ", couponDiscount=" + couponDiscount +
                ", charges=" + charges +
                ", totalAmount=" + totalAmount +
                ", transactionId=" + transactionId +
                '}';
    }
}