package com.drcita.user.models.coupns;

import java.util.List;

public class CouponData {
    private CouponModel coupon;
    private List<String> termsAndConditions;

    public CouponModel getCoupon() { return coupon; }
    public List<String> getTermsAndConditions() { return termsAndConditions; }
}
