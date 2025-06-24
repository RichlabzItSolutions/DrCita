package com.drcita.user.models.appointmentbookingsummary;

import java.util.List;

public class AppointmentsBookingResponse {
    private boolean success;
    private String message;
    private List<AppointmentBookingData> data;

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<AppointmentBookingData> getData() {
        return data;
    }
}
