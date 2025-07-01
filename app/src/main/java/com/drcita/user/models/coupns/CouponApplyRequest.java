package com.drcita.user.models.coupns;

public class CouponApplyRequest {
        private String couponCode;
        private int fee;
        private int userId;
        private int providerId;
        private int appointmentMode;
        private int appointmentType;

        public CouponApplyRequest(String couponCode,
                                  int fee,
                                  int userId,
                                  int providerId,
                                  int appointmentMode,
                                  int appointmentType) {
            this.couponCode      = couponCode;
            this.fee             = fee;
            this.userId          = userId;
            this.providerId      = providerId;
            this.appointmentMode = appointmentMode;
            this.appointmentType = appointmentType;
        }

        /* ——— getters (needed only if you’ll read them later) ——— */
        public String getCouponCode()     { return couponCode; }
        public int    getFee()            { return fee; }
        public int    getUserId()         { return userId; }
        public int    getProviderId()     { return providerId; }
        public int    getAppointmentMode(){ return appointmentMode; }
        public int    getAppointmentType(){ return appointmentType; }
    }


