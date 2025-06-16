package com.drcita.user.models.appointment;

import java.util.List;

public class TimePeriod {
    private int available;
    private List<TimeSlot> slots;

    // getters & setters
    public int getAvailable() { return available; }
    public void setAvailable(int available) { this.available = available; }

    public List<TimeSlot> getSlots() { return slots; }
    public void setSlots(List<TimeSlot> slots) { this.slots = slots; }

}
