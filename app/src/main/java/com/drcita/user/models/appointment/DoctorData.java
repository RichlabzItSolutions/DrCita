package com.drcita.user.models.appointment;

import java.util.List;

public class DoctorData {
    private int doctorId;
    private String picture;
    private String doctorName;
    private int experience;
    private double rating;
    private String gender;
    private int consultationMode;
    private List<String> specializations;
    private List<String> languages;
    private List<String> qualifications;
    private int providerId;
    private String hospitalName;
    private int onlineFee;
    private int offlineFee;
    private Slots slots;
    private int systemCharges;
    private int coupon;

    public int getCoupon() {
        return coupon;
    }

    public void setCoupon(int coupon) {
        this.coupon = coupon;
    }

    public int getSystemCharges() {
        return systemCharges;
    }

    public void setSystemCharges(int systemCharges) {
        this.systemCharges = systemCharges;
    }

    private Availability availability;


    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getConsultationMode() {
        return consultationMode;
    }

    public void setConsultationMode(int consultationMode) {
        this.consultationMode = consultationMode;
    }

    public List<String> getSpecializations() {
        return specializations;
    }

    public void setSpecializations(List<String> specializations) {
        this.specializations = specializations;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public List<String> getQualifications() {
        return qualifications;
    }

    public void setQualifications(List<String> qualifications) {
        this.qualifications = qualifications;
    }

    public int getProviderId() {
        return providerId;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public int getOnlineFee() {
        return onlineFee;
    }

    public void setOnlineFee(int onlineFee) {
        this.onlineFee = onlineFee;
    }

    public int getOfflineFee() {
        return offlineFee;
    }

    public void setOfflineFee(int offlineFee) {
        this.offlineFee = offlineFee;
    }

    public Slots getSlots() {
        return slots;
    }

    public void setSlots(Slots slots) {
        this.slots = slots;
    }

    public Availability getAvailability() {
        return availability;
    }

    public void setAvailability(Availability availability) {
        this.availability = availability;
    }

    @Override
    public String toString() {
        return "DoctorData{" +
                "doctorId=" + doctorId +
                ", picture='" + picture + '\'' +
                ", doctorName='" + doctorName + '\'' +
                ", experience=" + experience +
                ", rating=" + rating +
                ", gender='" + gender + '\'' +
                ", consultationMode=" + consultationMode +
                ", specializations=" + specializations +
                ", languages=" + languages +
                ", qualifications=" + qualifications +
                ", providerId=" + providerId +
                ", hospitalName='" + hospitalName + '\'' +
                ", onlineFee=" + onlineFee +
                ", offlineFee=" + offlineFee +
                ", slots=" + slots +
                ", availability=" + availability +
                '}';
    }
}
