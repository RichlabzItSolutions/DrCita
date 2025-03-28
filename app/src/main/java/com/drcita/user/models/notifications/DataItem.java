package com.drcita.user.models.notifications;

import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("image")
	private String image;

	@SerializedName("name")
	private String name;

	@SerializedName("description")
	private String description;

	@SerializedName("notificationDate")
	private String notficiationDate;

	@SerializedName("id")
	private int id;

	@SerializedName("title")
	private String title;

	public void setImage(String image) {
		this.image = image;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setNotficiationDate(String notficiationDate) {
		this.notficiationDate = notficiationDate;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImage(){
		return image;
	}

	public String getName(){
		return name;
	}

	public String getDescription(){
		return description;
	}

	public String getNotficiationDate(){
		return notficiationDate;
	}

	public int getId(){
		return id;
	}

	public String getTitle(){
		return title;
	}
}