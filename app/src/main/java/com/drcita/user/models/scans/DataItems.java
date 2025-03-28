package com.drcita.user.models.scans;

import org.parceler.Parcel;

@Parcel

public class DataItems{
	private String scanName;
	private String consultationCharges;
	private Long commission;
	private int scanId;
	private int scanStatus;
	private String hospitalName;
	private String region;
	private String picture;
	private String availableToday;
	private int providerId;

	public Long getCommission() {
		return commission;
	}

	public void setCommission(Long commission) {
		this.commission = commission;
	}

	public int getProviderId() {
		return providerId;
	}

	public void setProviderId(int providerId) {
		this.providerId = providerId;
	}

	public void setScanName(String scanName){
		this.scanName = scanName;
	}

	public String getScanName(){
		return scanName;
	}

	public void setConsultationCharges(String consultationCharges){
		this.consultationCharges = consultationCharges;
	}

	public String getConsultationCharges(){
		return consultationCharges;
	}

	public void setScanId(int scanId){
		this.scanId = scanId;
	}

	public int getScanId(){
		return scanId;
	}

	public void setScanStatus(int scanStatus){
		this.scanStatus = scanStatus;
	}

	public int getScanStatus(){
		return scanStatus;
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

	public void setAvailableToday(String availableToday){
		this.availableToday = availableToday;
	}

	public String getAvailableToday(){
		return availableToday;
	}

	@Override
	public String toString() {
		return "DataItems{" +
				"scanName='" + scanName + '\'' +
				", consultationCharges=" + consultationCharges +
				", scanId=" + scanId +
				", scanStatus=" + scanStatus +
				", hospitalName='" + hospitalName + '\'' +
				", region='" + region + '\'' +
				", picture='" + picture + '\'' +
				", availableToday='" + availableToday + '\'' +
				", providerId=" + providerId +
				'}';
	}
}
