package com.drcita.user.models.appointment;

public class Availability {
    private TimePeriod morning;
    private TimePeriod afternoon;
    private TimePeriod evening;

    // getters & setters
    public TimePeriod getMorning() { return morning; }
    public void setMorning(TimePeriod morning) { this.morning = morning; }

    public TimePeriod getAfternoon() { return afternoon; }
    public void setAfternoon(TimePeriod afternoon) { this.afternoon = afternoon; }

    public TimePeriod getEvening() { return evening; }
    public void setEvening(TimePeriod evening) { this.evening = evening; }



    @Override
    public String toString() {
        return "Availability{" +
                "morning=" + morning +
                ", afternoon=" + afternoon +
                ", evening=" + evening +
                '}';
    }
}
