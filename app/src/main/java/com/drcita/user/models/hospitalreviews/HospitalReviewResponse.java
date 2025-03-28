package com.drcita.user.models.hospitalreviews;

import java.util.List;

public class HospitalReviewResponse{
	private List<DataItem> data;
	private String message;
	private String status;

	public void setData(List<DataItem> data) {
		this.data = data;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<DataItem> getData(){
		return data;
	}

	public String getMessage(){
		return message;
	}

	public String getStatus(){
		return status;
	}
}