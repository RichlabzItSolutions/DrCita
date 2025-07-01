package com.drcita.user.models.coupns;

public class CouponModel {
    private int id;
    private String image;
    private String couponTitle;
    private String description;
    private int couponType;
    private String couponcode;
    private int couponWorth;
    private int minBill;
    private String validFrom;
    private String validTo;
    private int createdBy;
    private int status;
    private int applyFor;
    private int bookingtype;

    // Getter methods
    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getCouponTitle() {
        return couponTitle;
    }

    public String getDescription() {
        return description;
    }

    public int getCouponType() {
        return couponType;
    }

    public String getCouponcode() {
        return couponcode;
    }

    public int getCouponWorth() {
        return couponWorth;
    }

    public int getMinBill() {
        return minBill;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public String getValidTo() {
        return validTo;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public int getStatus() {
        return status;
    }

    public int getApplyFor() {
        return applyFor;
    }

    public int getBookingtype() {
        return bookingtype;
    }
}
