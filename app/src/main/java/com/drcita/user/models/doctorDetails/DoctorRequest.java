package com.drcita.user.models.doctorDetails;

public class DoctorRequest{
	private int doctorId;

	public void setDoctorId(int doctorId){
		this.doctorId = doctorId;
	}

	public int getDoctorId(){
		return doctorId;
	}

	@Override
 	public String toString(){
		return 
			"DoctorRequest{" + 
			"doctorId = '" + doctorId + '\'' + 
			"}";
		}
}
