package com.drcita.user.models.appointment;

public class DataItem{
	private String firstName;
	private String lastName;
	private String scanName;
	private int appointmentId;
	private int slotNumber;
	private String slotDate;
	private String picture;
	private String specializationName;
	private String hospitalName;
	private int appointmentType;
	private int appointmentStatus;

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getSpecializationName() {
		return specializationName;
	}

	public void setSpecializationName(String specializationName) {
		this.specializationName = specializationName;
	}

	public int getAppointmentType() {
		return appointmentType;
	}

	public void setAppointmentType(int appointmentType) {
		this.appointmentType = appointmentType;
	}

	public int getAppointmentStatus() {
		return appointmentStatus;
	}

	public void setAppointmentStatus(int appointmentStatus) {
		this.appointmentStatus = appointmentStatus;
	}

	public void setFirstName(String firstName){
		this.firstName = firstName;
	}

	public String getFirstName(){
		return firstName;
	}

	public void setLastName(String lastName){
		this.lastName = lastName;
	}

	public String getLastName(){
		return lastName;
	}

	public String getScanName() {
		return scanName;
	}

	public void setScanName(String scanName) {
		this.scanName = scanName;
	}

	public void setAppointmentId(int appointmentId){
		this.appointmentId = appointmentId;
	}

	public int getAppointmentId(){
		return appointmentId;
	}

	public void setSlotNumber(int slotNumber){
		this.slotNumber = slotNumber;
	}

	public int getSlotNumber(){
		return slotNumber;
	}

	public void setSlotDate(String slotDate){
		this.slotDate = slotDate;
	}

	public String getSlotDate(){
		return slotDate;
	}

	public void setPicture(String picture){
		this.picture = picture;
	}

	public String getPicture(){
		return picture;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"firstName = '" + firstName + '\'' + 
			",lastName = '" + lastName + '\'' + 
			",appointmentId = '" + appointmentId + '\'' + 
			",slotNumber = '" + slotNumber + '\'' + 
			",slotDate = '" + slotDate + '\'' + 
			",picture = '" + picture + '\'' + 
			"}";
		}
}
