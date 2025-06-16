package com.drcita.user.models.viewreceipt;

public class Appointment {

        private String appointmentId;
        private String doctorName;
        private String specialisation;
        private String scanName; // Nullable
        private String slotDate;
        private String slotTime;
        private String hospitalName;
        private String providerMobile;
        private String region;
        private String patientName;
        private String patientMobile;
        private String consultationFee;
        private int systemCharges;
        private int couponDiscount;
        private int totalAmount;
        private String transactionId;
        private String bookedOn;


    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getSpecialisation() {
        return specialisation;
    }

    public void setSpecialisation(String specialisation) {
        this.specialisation = specialisation;
    }

    public String getScanName() {
        return scanName;
    }

    public void setScanName(String scanName) {
        this.scanName = scanName;
    }

    public String getSlotDate() {
        return slotDate;
    }

    public void setSlotDate(String slotDate) {
        this.slotDate = slotDate;
    }

    public String getSlotTime() {
        return slotTime;
    }

    public void setSlotTime(String slotTime) {
        this.slotTime = slotTime;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getProviderMobile() {
        return providerMobile;
    }

    public void setProviderMobile(String providerMobile) {
        this.providerMobile = providerMobile;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientMobile() {
        return patientMobile;
    }

    public void setPatientMobile(String patientMobile) {
        this.patientMobile = patientMobile;
    }

    public String getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(String consultationFee) {
        this.consultationFee = consultationFee;
    }

    public int getSystemCharges() {
        return systemCharges;
    }

    public void setSystemCharges(int systemCharges) {
        this.systemCharges = systemCharges;
    }

    public int getCouponDiscount() {
        return couponDiscount;
    }

    public void setCouponDiscount(int couponDiscount) {
        this.couponDiscount = couponDiscount;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getBookedOn() {
        return bookedOn;
    }

    public void setBookedOn(String bookedOn) {
        this.bookedOn = bookedOn;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentId='" + appointmentId + '\'' +
                ", doctorName='" + doctorName + '\'' +
                ", specialisation='" + specialisation + '\'' +
                ", scanName='" + scanName + '\'' +
                ", slotDate='" + slotDate + '\'' +
                ", slotTime='" + slotTime + '\'' +
                ", hospitalName='" + hospitalName + '\'' +
                ", providerMobile='" + providerMobile + '\'' +
                ", region='" + region + '\'' +
                ", patientName='" + patientName + '\'' +
                ", patientMobile='" + patientMobile + '\'' +
                ", consultationFee='" + consultationFee + '\'' +
                ", systemCharges=" + systemCharges +
                ", couponDiscount=" + couponDiscount +
                ", totalAmount=" + totalAmount +
                ", transactionId='" + transactionId + '\'' +
                ", bookedOn='" + bookedOn + '\'' +
                '}';
    }
}
