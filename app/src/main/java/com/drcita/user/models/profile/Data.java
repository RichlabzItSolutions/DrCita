package com.drcita.user.models.profile;

public class Data{
	private String  gender;
	private String name;
	private String mobile;
	private int language;
	private int region;
	private int userId;
	private String picture;
	private int profileStatus;

	public int getProfileStatus() {
		return profileStatus;
	}

	public void setProfileStatus(int profileStatus) {
		this.profileStatus = profileStatus;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setLanguage(int language) {
		this.language = language;
	}

	public void setRegion(int region) {
		this.region = region;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getGender(){
		return gender;
	}

	public String getName(){
		return name;
	}

	public String getMobile(){
		return mobile;
	}

	public int getLanguage(){
		return language;
	}

	public int getRegion(){
		return region;
	}

	public int getUserId(){
		return userId;
	}

	public String getPicture(){
		return picture;
	}
}
