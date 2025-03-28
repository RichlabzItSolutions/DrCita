package com.drcita.user.models.scans;

import java.util.List;

public class ScansListResponse{
	private List<DataItems> data;
	private String message;
	private String status;

	public void setData(List<DataItems> data){
		this.data = data;
	}

	public List<DataItems> getData(){
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