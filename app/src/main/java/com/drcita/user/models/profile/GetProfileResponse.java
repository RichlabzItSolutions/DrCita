package com.drcita.user.models.profile;

public class GetProfileResponse{
	private Data data;
	private String message;
	private String status;

	public Data getData(){
		return data;
	}

	public String getMessage(){
		return message;
	}

	public String getStatus(){
		return status;
	}
}
