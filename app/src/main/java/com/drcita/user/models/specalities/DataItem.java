package com.drcita.user.models.specalities;

public class DataItem{
	private String updatedAt;
	private String name;
	private String createdAt;
	private int id;
	private String picture;
	private int status;

	public String getUpdatedAt(){
		return updatedAt;
	}

	public String getName(){
		return name;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public int getId(){
		return id;
	}

	public String getPicture(){
		return picture;
	}

	public int getStatus(){
		return status;
	}
}
