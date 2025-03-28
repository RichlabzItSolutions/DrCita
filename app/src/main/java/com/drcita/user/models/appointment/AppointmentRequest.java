package com.drcita.user.models.appointment;

public class AppointmentRequest{
	private int userId;
	private int userType;

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public void setUserId(int userId){
		this.userId = userId;
	}

	public int getUserId(){
		return userId;
	}

	@Override
 	public String toString(){
		return 
			"AppointmentRequest{" + 
			"userId = '" + userId + '\'' + 
			"}";
		}
}
