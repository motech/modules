package org.motechproject.pillreminder.domain;

import org.motechproject.mds.annotations.CrudEvents;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.event.CrudEventType;

@Entity
@CrudEvents(CrudEventType.NONE)
public class DailyScheduleDetails {

    @Field(displayName = "Pill Window in Hours")
    private int pillWindowInHours;

    @Field(displayName = "Repeat Interval in Minutes")
    private int repeatIntervalInMinutes;

    @Field(displayName = "Buffer Over-Dosage Time in Minutes")
    private int bufferOverDosageTimeInMinutes;

    public DailyScheduleDetails() {
    }

    public DailyScheduleDetails(int repeatIntervalInMinutes, int pillWindowInHours, int bufferOverDosageTimeInMinutes) {
        this.repeatIntervalInMinutes = repeatIntervalInMinutes;
        this.pillWindowInHours = pillWindowInHours;
        this.bufferOverDosageTimeInMinutes = bufferOverDosageTimeInMinutes;
    }

    public int getPillWindowInHours() {
        return pillWindowInHours;
    }

    public void setPillWindowInHours(int pillWindowInHours) {
        this.pillWindowInHours = pillWindowInHours;
    }

    public int getRepeatIntervalInMinutes() {
        return repeatIntervalInMinutes;
    }

    public void setRepeatIntervalInMinutes(int repeatIntervalInMinutes) {
        this.repeatIntervalInMinutes = repeatIntervalInMinutes;
    }

    public int getBufferOverDosageTimeInMinutes() {
        return bufferOverDosageTimeInMinutes;
    }

    public void setBufferOverDosageTimeInMinutes(int bufferOverDosageTimeInMinutes) {
        this.bufferOverDosageTimeInMinutes = bufferOverDosageTimeInMinutes;
    }

}
