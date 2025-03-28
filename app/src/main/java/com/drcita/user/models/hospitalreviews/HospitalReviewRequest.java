package com.drcita.user.models.hospitalreviews;

public class HospitalReviewRequest{
	private int providerId;
	private int specialisationId;
	private int region;
	private String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getRegion() {
		return region;
	}

	public void setRegion(int region) {
		this.region = region;
	}

	public void setProviderId(int providerId) {
		this.providerId = providerId;
	}

	public int getProviderId(){
		return providerId;
	}

	public int getSpecialisationId() {
		return specialisationId;
	}

	public void setSpecialisationId(int specialisationId) {
		this.specialisationId = specialisationId;
	}
}
