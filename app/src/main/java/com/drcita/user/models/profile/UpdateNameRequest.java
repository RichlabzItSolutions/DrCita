package com.drcita.user.models.profile;

public class UpdateNameRequest{
	private String name;
	private int userId;

	public String getName(){
		return name;
	}

	public int getUserId(){
		return userId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
}
