package com.drcita.user.models.profile;

import com.google.gson.annotations.SerializedName;

public class UpdateProfileResponse{

	@SerializedName("data")
	private Object data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private String status;

	public Object getData(){
		return data;
	}

	public String getMessage(){
		return message;
	}

	public String getStatus(){
		return status;
	}
}