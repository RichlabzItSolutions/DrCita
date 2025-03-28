package com.drcita.user.models.profile;

import com.google.gson.annotations.SerializedName;

public class UpdateProfileRequest{

	@SerializedName("gender")
	private String gender;

	@SerializedName("language")
	private int language;

	@SerializedName("region")
	private int region;

	@SerializedName("userId")
	private String userId;

	@SerializedName("dob")
	private String dob;

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getLanguage() {
		return language;
	}

	public void setLanguage(int language) {
		this.language = language;
	}

	public int getRegion() {
		return region;
	}

	public void setRegion(int region) {
		this.region = region;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	@Override
	public String toString() {
		return "UpdateProfileRequest{" +
				"gender='" + gender + '\'' +
				", language=" + language +
				", region=" + region +
				", userId='" + userId + '\'' +
				", dob='" + dob + '\'' +
				'}';
	}
}