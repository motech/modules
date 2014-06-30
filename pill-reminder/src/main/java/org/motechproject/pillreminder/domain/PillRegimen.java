package org.motechproject.pillreminder.domain;

import org.joda.time.DateTime;
import org.motechproject.commons.date.model.Time;
import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.mds.annotations.Cascade;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import java.util.Set;

@Entity
public class PillRegimen {

    @Field
    private Long id;

    @Field(displayName = "External ID")
    private String externalId;

    @Field(displayName = "Schedule Details")
    @Cascade(delete = true)
    private DailyScheduleDetails scheduleDetails;

    @Field(displayName = "Dosages")
    @Cascade(delete = true)
    private Set<Dosage> dosages;

    public PillRegimen() {
    }

    public PillRegimen(String externalId, Set<Dosage> dosages, DailyScheduleDetails scheduleDetails) {
        this.externalId = externalId;
        this.dosages = dosages;
        this.scheduleDetails = scheduleDetails;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public Set<Dosage> getDosages() {
        return dosages;
    }

    public void setDosages(Set<Dosage> dosages) {
        this.dosages = dosages;
    }

    public DailyScheduleDetails getScheduleDetails() {
        return scheduleDetails;
    }

    public void setScheduleDetails(DailyScheduleDetails scheduleDetails) {
        this.scheduleDetails = scheduleDetails;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void validate() {
        for (Dosage dosage : dosages) {
            dosage.validate();
        }
    }

    public Dosage getDosage(Long dosageId) {
        for (Dosage dosage : dosages) {
            if (dosage.getId().equals(dosageId)) {
                return dosage;
            }
        }
        return null;
    }

    public boolean isFirstReminderFor(Dosage dosage) {
        return numberOfTimesPillRemindersSentFor(dosage) == 0;
    }

    private int getOffsetOfCurrentTimeFromDosageStartTime(Time dosageStartTime, DateTime now) {
        int hourDiff = now.getHourOfDay() - dosageStartTime.getHour();
        if (hourDiff < 0) {
            hourDiff += 24;
        }
        return hourDiff * 60 + now.getMinuteOfHour() - dosageStartTime.getMinute();
    }

    public int numberOfTimesPillRemindersSentFor(Dosage dosage) {
        DailyScheduleDetails details = getScheduleDetails();
        Time dosageStartTime = dosage.getDosageTime();
        int minsSinceDosage = Math.min(getOffsetOfCurrentTimeFromDosageStartTime(dosageStartTime, DateUtil.now()), details.getPillWindowInHours() * 60);
        return (minsSinceDosage / details.getRepeatIntervalInMinutes());
    }

    public int timesPillRemainderWillBeSent() {
        DailyScheduleDetails details = getScheduleDetails();
        return (details.getPillWindowInHours() * 60) / details.getRepeatIntervalInMinutes();
    }
}
