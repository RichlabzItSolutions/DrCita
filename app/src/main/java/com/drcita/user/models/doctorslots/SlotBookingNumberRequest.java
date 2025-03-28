package com.drcita.user.models.doctorslots;

import com.google.gson.annotations.SerializedName;

public class SlotBookingNumberRequest{

	@SerializedName("doctorId")
	private int doctorId;

	@SerializedName("providerId")
	private int providerId;

	@SerializedName("slotId")
	private int slotId;

	@SerializedName("appointmentDate")
	private String appointmentDate;

	public int getDoctorId(){
		return doctorId;
	}

	public int getProviderId(){
		return providerId;
	}

	public int getSlotId(){
		return slotId;
	}

	public String getAppointmentDate(){
		return appointmentDate;
	}

	public void setDoctorId(int doctorId) {
		this.doctorId = doctorId;
	}

	public void setProviderId(int providerId) {
		this.providerId = providerId;
	}

	public void setSlotId(int slotId) {
		this.slotId = slotId;
	}

	public void setAppointmentDate(String appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	@Override
	public String toString() {
		return "SlotBookingNumberRequest{" +
				"doctorId=" + doctorId +
				", providerId=" + providerId +
				", slotId=" + slotId +
				", appointmentDate='" + appointmentDate + '\'' +
				'}';
	}
}