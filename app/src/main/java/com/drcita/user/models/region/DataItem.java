package com.drcita.user.models.region;

import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("name")
	private String name;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	@SerializedName("status")
	private int status;
	@SerializedName("currency_type")

	private int currencyType;

	public int getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(int currencyType) {
		this.currencyType = currencyType;
	}

	private boolean isSelected;
	private boolean isSelectedcurrency1=false;

	private  boolean isSelectedcurrency2=false;

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean selected) {
		isSelected = selected;
	}

	public boolean isSelectedcurrency1() {
		return isSelectedcurrency1;
	}

	public void setSelectedcurrency1(boolean selectedcurrency1) {
		isSelectedcurrency1 = selectedcurrency1;
	}

	public boolean isSelectedcurrency2() {
		return isSelectedcurrency2;
	}

	public void setSelectedcurrency2(boolean selectedcurrency2) {
		isSelectedcurrency2 = selectedcurrency2;
	}

	@Override
	public String toString() {
		return "DataItem{" +
				"updatedAt='" + updatedAt + '\'' +
				", name='" + name + '\'' +
				", createdAt='" + createdAt + '\'' +
				", id=" + id +
				", status=" + status +
				", currencyType=" + currencyType +
				", isSelected=" + isSelected +
				", isSelectedcurrency1=" + isSelectedcurrency1 +
				", isSelectedcurrency2=" + isSelectedcurrency2 +
				'}';
	}
}