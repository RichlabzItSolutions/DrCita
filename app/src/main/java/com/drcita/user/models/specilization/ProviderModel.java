package com.drcita.user.models.specilization;

public class ProviderModel {
    public int doctorId;
    public int providerId;
    public String hospitalName;
    public int onlineFee;
    public int offlineFee;
    public int consultationMode;
    public int morningAvailable;
    public int afternoonAvailable;
    public int eveningAvailable;

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

    public int getConsultationMode() {
        return consultationMode;
    }

    public void setConsultationMode(int consultationMode) {
        this.consultationMode = consultationMode;
    }

    public int getMorningAvailable() {
        return morningAvailable;
    }

    public void setMorningAvailable(int morningAvailable) {
        this.morningAvailable = morningAvailable;
    }

    public int getAfternoonAvailable() {
        return afternoonAvailable;
    }

    public void setAfternoonAvailable(int afternoonAvailable) {
        this.afternoonAvailable = afternoonAvailable;
    }

    public int getEveningAvailable() {
        return eveningAvailable;
    }

    public void setEveningAvailable(int eveningAvailable) {
        this.eveningAvailable = eveningAvailable;
    }

    @Override
    public String toString() {
        return "ProviderModel{" +
                "doctorId=" + doctorId +
                ", providerId=" + providerId +
                ", hospitalName='" + hospitalName + '\'' +
                ", onlineFee=" + onlineFee +
                ", offlineFee=" + offlineFee +
                ", consultationMode=" + consultationMode +
                ", morningAvailable=" + morningAvailable +
                ", afternoonAvailable=" + afternoonAvailable +
                ", eveningAvailable=" + eveningAvailable +
                '}';
    }
}
