package com.drcita.user.models.hospitals;

public class HospitalsRequest{
	private int region;
	private int specialisationId;

	public int getSpecialisationId() {
		return specialisationId;
	}

	public void setSpecialisationId(int specialisationId) {
		this.specialisationId = specialisationId;
	}

	public void setRegion(int region) {
		this.region = region;
	}

	public int getRegion(){
		return region;
	}
}
