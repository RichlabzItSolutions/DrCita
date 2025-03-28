package com.drcita.user.models.doctorslots;

public class DoctorslotsListRequest{
	private int doctorId;
	private String appointmentDate;
	private int appointmentType;
	private String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setDoctorId(int doctorId) {
		this.doctorId = doctorId;
	}

	public void setAppointmentDate(String appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public int getDoctorId(){
		return doctorId;
	}

	public String getAppointmentDate(){
		return appointmentDate;
	}

	public int getAppointmentType() {
		return appointmentType;
	}

	public void setAppointmentType(int appointmentType) {
		this.appointmentType = appointmentType;
	}
}
