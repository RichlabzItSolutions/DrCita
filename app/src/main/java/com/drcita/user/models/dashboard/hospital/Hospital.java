package com.drcita.user.models.dashboard.hospital;

public class Hospital {
    private final int logoResId;

    public Hospital(int logoResId) {
        this.logoResId = logoResId;
    }

    public int getLogoResId() {
        return logoResId;
    }
}