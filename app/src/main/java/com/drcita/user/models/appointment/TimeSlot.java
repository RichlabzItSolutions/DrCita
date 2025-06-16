package com.drcita.user.models.appointment;

public class TimeSlot {
    private String slot;
    private int isAvailable;

    // getters & setters
    public String getSlot() { return slot; }
    public void setSlot(String slot) { this.slot = slot; }

    public int getIsAvailable() { return isAvailable; }
    public void setIsAvailable(int isAvailable) { this.isAvailable = isAvailable; }
}
