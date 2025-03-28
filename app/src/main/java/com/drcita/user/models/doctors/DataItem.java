package com.drcita.user.models.doctors;
import org.parceler.Parcel;
@Parcel
public class DataItem{
	public String firstName;
	public String lastName;
	public int ratedCount;
	public int doctorId;
	public String consultationCharges;
	public int mobile;
	public int rating;
	public int experience;
	public String picture;
	public String availableToday;
	public String specialisationName;
	public String hospitalName;
	public String region;
	public int providerId;

	public int getProviderId() {
		return providerId;
	}

	public void setProviderId(int providerId) {
		this.providerId = providerId;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setRatedCount(int ratedCount) {
		this.ratedCount = ratedCount;
	}

	public void setDoctorId(int doctorId) {
		this.doctorId = doctorId;
	}

	public void setConsultationCharges(String consultationCharges) {
		this.consultationCharges = consultationCharges;
	}

	public void setMobile(int mobile) {
		this.mobile = mobile;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public void setAvailableToday(String availableToday) {
		this.availableToday = availableToday;
	}

	public String getSpecialisationName() {
		return specialisationName;
	}

	public void setSpecialisationName(String specialisationName) {
		this.specialisationName = specialisationName;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getFirstName(){
		return firstName;
	}

	public String getLastName(){
		return lastName;
	}

	public int getRatedCount(){
		return ratedCount;
	}

	public int getDoctorId(){
		return doctorId;
	}

	public String getConsultationCharges(){
		return consultationCharges;
	}

	public int getMobile(){
		return mobile;
	}

	public int getRating(){
		return rating;
	}

	public int getExperience(){
		return experience;
	}

	public String getPicture(){
		return picture;
	}

	public String getAvailableToday(){
		return availableToday;
	}

	@Override
	public String toString() {
		return "DataItem{" +
				"firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", ratedCount=" + ratedCount +
				", doctorId=" + doctorId +
				", consultationCharges=" + consultationCharges +
				", mobile=" + mobile +
				", rating=" + rating +
				", experience=" + experience +
				", picture='" + picture + '\'' +
				", availableToday='" + availableToday + '\'' +
				", specialisationName='" + specialisationName + '\'' +
				", hospitalName='" + hospitalName + '\'' +
				", region='" + region + '\'' +
				", providerId=" + providerId +
				'}';
	}
}
