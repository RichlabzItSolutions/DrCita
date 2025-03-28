package com.drcita.user.models.scans;

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

	public void setRatedCount(int ratedCount){
		this.ratedCount = ratedCount;
	}

	public int getRatedCount(){
		return ratedCount;
	}

	public void setProviderId(int providerId){
		this.providerId = providerId;
	}

	public int getProviderId(){
		return providerId;
	}

	public void setMobile(String mobile){
		this.mobile = mobile;
	}

	public String getMobile(){
		return mobile;
	}

	public void setRating(double rating){
		this.rating = rating;
	}

	public double getRating(){
		return rating;
	}

	public void setOpeningHours(String openingHours){
		this.openingHours = openingHours;
	}

	public String getOpeningHours(){
		return openingHours;
	}

	public void setHospitalName(String hospitalName){
		this.hospitalName = hospitalName;
	}

	public String getHospitalName(){
		return hospitalName;
	}

	public void setRegion(String region){
		this.region = region;
	}

	public String getRegion(){
		return region;
	}

	public void setPicture(String picture){
		this.picture = picture;
	}

	public String getPicture(){
		return picture;
	}
}
