package com.drcita.user.models.medicalrecords;

import java.util.List;

public class GetMedicalRecordsResponse{
	private List<DataItem> data;
	private String message;
	private String status;

	public void setData(List<DataItem> data){
		this.data = data;
	}

	public List<DataItem> getData(){
		return data;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}
}