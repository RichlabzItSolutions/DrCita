package com.drcita.user.models.userprofile;

import java.util.List;

public class MemberResponse {
    private boolean success;
    private String message;
    private List<Member> data;

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public List<Member> getData() { return data; }
    public void setData(List<Member> data) { this.data = data; }
}
