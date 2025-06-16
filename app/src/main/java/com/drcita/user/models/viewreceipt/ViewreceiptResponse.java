package com.drcita.user.models.viewreceipt;

public class ViewreceiptResponse{
	private boolean success;
	private String message;
	private AppointmentDetails data;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public AppointmentDetails getData() {
		return data;
	}

	public void setData(AppointmentDetails data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ViewreceiptResponse{" +
				"success=" + success +
				", message='" + message + '\'' +
				", data=" + data +
				'}';
	}
}
