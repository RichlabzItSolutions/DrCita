package com.drcita.user.models.appointment;

public class DocterDetailResoponse {

    private boolean success;
    private String message;
    private DoctorData data;

    // getters & setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public DoctorData getData() { return data; }
    public void setData(DoctorData data) { this.data = data; }

    @Override
    public String toString() {
        return "DocterDetailResoponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
