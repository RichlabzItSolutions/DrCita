package com.drcita.user.models.doctorslots;

import java.util.List;

public class DoctorslotsListResponse{
	private List<DataItem> data;
	private String message;
	private String status;
	private String slotNumber;

	public List<DataItem> getData(){
		return data;
	}

	public String getMessage(){
		return message;
	}

	public String getStatus(){
		return status;
	}

	public void setData(List<DataItem> data) {
		this.data = data;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSlotNumber() {
		return slotNumber;
	}

	public void setSlotNumber(String slotNumber) {
		this.slotNumber = slotNumber;
	}
}