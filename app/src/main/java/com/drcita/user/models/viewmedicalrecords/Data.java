package com.drcita.user.models.viewmedicalrecords;

import java.util.ArrayList;
import java.util.List;

public class Data{
	private String reason;
	private ArrayList<DocsItem> docs;
	private int appointmentId;
	private String description;
	private ArrayList<PhotosItem> photos;
	private String addedOn;

	public List<DocsItem> getDocs() {
		return docs;
	}

	public List<PhotosItem> getPhotos() {
		return photos;
	}

	public void setReason(String reason){
		this.reason = reason;
	}

	public String getReason(){
		return reason;
	}

	public void setData(ArrayList<DocsItem> docs) {
		this.docs = docs;
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

	public void setPhotos(ArrayList<PhotosItem> photos){
		this.photos = photos;
	}



	public void setAddedOn(String addedOn){
		this.addedOn = addedOn;
	}

	public String getAddedOn(){
		return addedOn;
	}
}