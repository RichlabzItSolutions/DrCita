package com.drcita.user.models.coupns;

import java.util.List;

public class CouponResponse {
    private boolean success;
    private String message;
    private List<CouponModel> data;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<CouponModel> getData() {
        return data;
    }
}
