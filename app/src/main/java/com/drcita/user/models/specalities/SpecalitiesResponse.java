package com.drcita.user.models.specalities;

import java.util.List;

public class SpecalitiesResponse{
	private List<DataItem> data;
	private String message;
	private String status;

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