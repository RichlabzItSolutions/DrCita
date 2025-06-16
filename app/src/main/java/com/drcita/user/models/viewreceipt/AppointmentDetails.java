package com.drcita.user.models.viewreceipt;

import java.util.List;

public class AppointmentDetails {
    private Appointment appointment;
    private List<Notes> notes;


    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public List<Notes> getNotes() {
        return notes;
    }

    public void setNotes(List<Notes> notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "AppointmentDetails{" +
                "appointment=" + appointment +
                ", notes=" + notes +
                '}';
    }
}
