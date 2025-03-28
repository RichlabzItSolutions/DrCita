package com.drcita.user.models.doctorDetails;

public class Data{
	private String firstName;
	private String lastName;
	private int ratedCount;
	private int doctorId;
	private int consultationCharges;
	private Object mobile;
	private int rating;
	private int experience;
	private String availableDays;
	private String picture;
	private String availableToday;

	public void setFirstName(String firstName){
		this.firstName = firstName;
	}

	public String getFirstName(){
		return firstName;
	}

	public void setLastName(String lastName){
		this.lastName = lastName;
	}

	public String getLastName(){
		return lastName;
	}

	public void setRatedCount(int ratedCount){
		this.ratedCount = ratedCount;
	}

	public int getRatedCount(){
		return ratedCount;
	}

	public void setDoctorId(int doctorId){
		this.doctorId = doctorId;
	}

	public int getDoctorId(){
		return doctorId;
	}

	public void setConsultationCharges(int consultationCharges){
		this.consultationCharges = consultationCharges;
	}

	public int getConsultationCharges(){
		return consultationCharges;
	}

	public void setMobile(Object mobile){
		this.mobile = mobile;
	}

	public Object getMobile(){
		return mobile;
	}

	public void setRating(int rating){
		this.rating = rating;
	}

	public int getRating(){
		return rating;
	}

	public void setExperience(int experience){
		this.experience = experience;
	}

	public int getExperience(){
		return experience;
	}

	public void setAvailableDays(String availableDays){
		this.availableDays = availableDays;
	}

	public String getAvailableDays(){
		return availableDays;
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
 	public String toString(){
		return 
			"Data{" + 
			"firstName = '" + firstName + '\'' + 
			",lastName = '" + lastName + '\'' + 
			",ratedCount = '" + ratedCount + '\'' + 
			",doctorId = '" + doctorId + '\'' + 
			",consultationCharges = '" + consultationCharges + '\'' + 
			",mobile = '" + mobile + '\'' + 
			",rating = '" + rating + '\'' + 
			",experience = '" + experience + '\'' + 
			",availableDays = '" + availableDays + '\'' + 
			",picture = '" + picture + '\'' + 
			",availableToday = '" + availableToday + '\'' + 
			"}";
		}
}
