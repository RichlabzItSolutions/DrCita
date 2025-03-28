package com.drcita.user.models.hospitals;

import org.parceler.Parcel;

@Parcel
public class DataItem{
	public int ratedCount;
	public int providerId;
	public String mobile;
	public double rating;
	public String openingHours;
	public String hospitalName;
	public String region;
	public String picture;
	public String free;

	public String getFree() {
		return free;
	}

	public void setFree(String free) {
		this.free = free;
	}

	public void setRatedCount(int ratedCount) {
		this.ratedCount = ratedCount;
	}

	public void setProviderId(int providerId) {
		this.providerId = providerId;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public void setOpeningHours(String openingHours) {
		this.openingHours = openingHours;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public int getRatedCount(){
		return ratedCount;
	}

	public int getProviderId(){
		return providerId;
	}

	public String getMobile(){
		return mobile;
	}

	public double getRating(){
		return rating;
	}

	public String getOpeningHours(){
		return openingHours;
	}

	public String getHospitalName(){
		return hospitalName;
	}

	public String getRegion(){
		return region;
	}

	public String getPicture(){
		return picture;
	}
}
