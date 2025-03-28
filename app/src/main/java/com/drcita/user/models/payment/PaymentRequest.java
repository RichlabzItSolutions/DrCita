package com.drcita.user.models.payment;

public class PaymentRequest{
	private int id;
	private int userId;
	private int appointmentType;
	private int paymentGatewayType;
	private String mobile;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public int getPaymentGatewayType() {
		return paymentGatewayType;
	}

	public void setPaymentGatewayType(int paymentGatewayType) {
		this.paymentGatewayType = paymentGatewayType;
	}

	public int getAppointmentType() {
		return appointmentType;
	}

	public void setAppointmentType(int appointmentType) {
		this.appointmentType = appointmentType;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getId(){
		return id;
	}

	public int getUserId(){
		return userId;
	}
}
