package com.drcita.user.models.medicalrecords;

import java.util.List;

public class GetMedicalRecordsResponse{
		private boolean success;
		private String message;
		private List<MedicalRecordData>  data;

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

	public List<MedicalRecordData> getData() {
		return data;
	}

	public void setData(List<MedicalRecordData> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "GetMedicalRecordsResponse{" +
				"success=" + success +
				", message='" + message + '\'' +
				", data=" + data +
				'}';
	}
}