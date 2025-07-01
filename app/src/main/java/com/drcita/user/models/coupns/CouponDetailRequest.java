package com.drcita.user.models.coupns;

public class CouponDetailRequest {

    private String couponId;

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public CouponDetailRequest(String couponId) {
        this.couponId = couponId;
    }
}
