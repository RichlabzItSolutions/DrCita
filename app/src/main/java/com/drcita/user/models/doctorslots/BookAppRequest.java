package com.drcita.user.models.doctorslots;

import com.google.gson.annotations.SerializedName;

public class BookAppRequest{

	@SerializedName("doctorId")
	private int doctorId;

	@SerializedName("providerId")
	private int providerId;

	@SerializedName("appointmentMode")
	private int appointmentMode;

	@SerializedName("slotNumber")
	private int slotNumber;


	@SerializedName("userId")
	private int userId;

	@SerializedName("appointmentDate")
	private String appointmentDate;

	@SerializedName("appointmentType")
	private int appointmentType;

	@SerializedName("patientName")
	private String patientName;

	@SerializedName("patientMobile")
	private String patientMobile;

	public String getPatientMobile() {
		return patientMobile;
	}

	public void setPatientMobile(String patientMobile) {
		this.patientMobile = patientMobile;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public int getAppointmentType() {
		return appointmentType;
	}

	public void setAppointmentType(int appointmentType) {
		this.appointmentType = appointmentType;
	}

	public void setDoctorId(int doctorId){
		this.doctorId = doctorId;
	}

	public int getDoctorId(){
		return doctorId;
	}

	public void setProviderId(int providerId){
		this.providerId = providerId;
	}

	public int getProviderId(){
		return providerId;
	}

	public void setAppointmentMode(int appointmentMode){
		this.appointmentMode = appointmentMode;
	}

	public int getAppointmentMode(){
		return appointmentMode;
	}

	public void setSlotNumber(int slotNumber){
		this.slotNumber = slotNumber;
	}

	public int getSlotNumber(){
		return slotNumber;
	}


	public void setUserId(int userId){
		this.userId = userId;
	}

	public int getUserId(){
		return userId;
	}

	public void setAppointmentDate(String appointmentDate){
		this.appointmentDate = appointmentDate;
	}

	public String getAppointmentDate(){
		return appointmentDate;
	}

	@Override
 	public String toString(){
		return 
			"BookAppRequest{" + 
			"doctorId = '" + doctorId + '\'' + 
			",providerId = '" + providerId + '\'' + 
			",appointmentMode = '" + appointmentMode + '\'' + 
			",slotNumber = '" + slotNumber + '\'' + 
			",userId = '" + userId + '\'' +
			",appointmentDate = '" + appointmentDate + '\'' + 
			"}";
		}
}