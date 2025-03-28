package com.drcita.user.models.medicalrecords;

import java.util.List;

public class DataItem{
	private Object scanName;
	private String reason;
	private List<Object> docs;
	private int appointmentId;
	private String description;
	private String hospitalName;
	private String doctorName;
	private List<Object> photos;
	private int medicalRecordId;
	private String addedOn;

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public void setScanName(Object scanName){
		this.scanName = scanName;
	}

	public Object getScanName(){
		return scanName;
	}

	public void setReason(String reason){
		this.reason = reason;
	}

	public String getReason(){
		return reason;
	}

	public void setDocs(List<Object> docs){
		this.docs = docs;
	}

	public List<Object> getDocs(){
		return docs;
	}

	public void setAppointmentId(int appointmentId){
		this.appointmentId = appointmentId;
	}

	public int getAppointmentId(){
		return appointmentId;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setHospitalName(String hospitalName){
		this.hospitalName = hospitalName;
	}

	public String getHospitalName(){
		return hospitalName;
	}

	public void setPhotos(List<Object> photos){
		this.photos = photos;
	}

	public List<Object> getPhotos(){
		return photos;
	}

	public void setMedicalRecordId(int medicalRecordId){
		this.medicalRecordId = medicalRecordId;
	}

	public int getMedicalRecordId(){
		return medicalRecordId;
	}

	public void setAddedOn(String addedOn){
		this.addedOn = addedOn;
	}

	public String getAddedOn(){
		return addedOn;
	}
}