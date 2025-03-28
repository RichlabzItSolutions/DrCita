package com.drcita.user.models.viewreceipt;

public class Data{
	private String patientName;
	private String doctorName;
	private String specialisation;
	private String scanName;
	private String bookedOn;
	private int appointmentId;
	private int slotNumber;
	private String slotDate;
	private String paymentMethod;
	private String hospitalName;
	private String region;
	private int consultationFee;
	private String providerMobile;

	public String getProviderMobile() {
		return providerMobile;
	}

	public void setProviderMobile(String providerMobile) {
		this.providerMobile = providerMobile;
	}

	public String getScanName() {
		return scanName;
	}

	public void setScanName(String scanName) {
		this.scanName = scanName;
	}

	public void setPatientName(String patientName){
		this.patientName = patientName;
	}

	public String getPatientName(){
		return patientName;
	}

	public void setDoctorName(String doctorName){
		this.doctorName = doctorName;
	}

	public String getDoctorName(){
		return doctorName;
	}

	public void setSpecialisation(String specialisation){
		this.specialisation = specialisation;
	}

	public String getSpecialisation(){
		return specialisation;
	}

	public void setBookedOn(String bookedOn){
		this.bookedOn = bookedOn;
	}

	public String getBookedOn(){
		return bookedOn;
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

	public void setPaymentMethod(String paymentMethod){
		this.paymentMethod = paymentMethod;
	}

	public String getPaymentMethod(){
		return paymentMethod;
	}

	public void setHospitalName(String hospitalName){
		this.hospitalName = hospitalName;
	}

	public String getHospitalName(){
		return hospitalName;
	}

	public void setRegion(String region){
		this.region = region;
	}

	public String getRegion(){
		return region;
	}

	public void setConsultationFee(int consultationFee){
		this.consultationFee = consultationFee;
	}

	public int getConsultationFee(){
		return consultationFee;
	}
}
