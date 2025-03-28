package com.drcita.user.models.doctorslots;

import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("to_time")
	private String toTime;
	@SerializedName("from_time")
	private String fromTime;
	@SerializedName("id")
	private String id;
	@SerializedName("day")
	private String day;
	@SerializedName("slot")
	private String slot;
	public void setToTime(String toTime) {
		this.toTime = toTime;
	}

	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}

	public String getToTime(){
		return toTime;
	}

	public String getFromTime(){
		return fromTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getSlot() {
		return slot;
	}

	public void setSlot(String slot) {
		this.slot = slot;
	}
}
