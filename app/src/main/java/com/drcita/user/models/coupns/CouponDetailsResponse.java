package com.drcita.user.models.coupns;

public class CouponDetailsResponse {
    private boolean success;
    private String message;
    private CouponData data;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public CouponData getData() { return data; }
}
