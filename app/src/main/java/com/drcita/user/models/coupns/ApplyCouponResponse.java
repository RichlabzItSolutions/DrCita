package com.drcita.user.models.coupns;

public class ApplyCouponResponse {
    private boolean success;
    private String message;
    private CouponDiscountData data;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public CouponDiscountData getData() {
        return data;
    }
}

