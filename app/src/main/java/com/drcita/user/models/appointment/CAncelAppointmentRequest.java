package com.drcita.user.models.appointment;

public class CAncelAppointmentRequest{
	private int appointmentId;
	private int userId;

	public void setAppointmentId(int appointmentId){
		this.appointmentId = appointmentId;
	}

	public int getAppointmentId(){
		return appointmentId;
	}

	public void setUserId(int userId){
		this.userId = userId;
	}

	public int getUserId(){
		return userId;
	}
}
