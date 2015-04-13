package org.motechproject.pillreminder.domain;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.motechproject.commons.date.model.Time;
import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.mds.annotations.Cascade;
import org.motechproject.mds.annotations.CrudEvents;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.Ignore;
import org.motechproject.mds.event.CrudEventType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@CrudEvents(CrudEventType.NONE)
public class Dosage {

    @Field
    private Long id;

    @Field(displayName = "Dosage Time")
    private Time dosageTime;

    @Field(displayName = "Last Response Captured Date")
    private LocalDate responseLastCapturedDate;

    @Field(displayName = "Medicines")
    @Cascade(delete = true)
    private Set<Medicine> medicines;

    public Dosage() {
    }

    public Dosage(Time dosageTime, Set<Medicine> medicines) {
        this.dosageTime = dosageTime;
        this.medicines = medicines;
    }

    public Set<Medicine> getMedicines() {
        return medicines;
    }

    public void setMedicines(Set<Medicine> medicines) {
        this.medicines = medicines;
    }

    public Time getDosageTime() {
        return dosageTime;
    }

    public void setDosageTime(Time dosageTime) {
        this.dosageTime = dosageTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getResponseLastCapturedDate() {
        return responseLastCapturedDate;
    }

    public void setResponseLastCapturedDate(LocalDate responseLastCapturedDate) {
        this.responseLastCapturedDate = responseLastCapturedDate;
    }

    public void updateResponseLastCapturedDate(LocalDate lastCapturedDate) {
        if (responseLastCapturedDate == null || responseLastCapturedDate.isBefore(lastCapturedDate)) {
            responseLastCapturedDate = lastCapturedDate;
        }
    }

    @Ignore
    public boolean isTodaysDosageResponseCaptured() {
        LocalDate today = DateUtil.today();
        LocalDate yesterday = today.minusDays(1);
        LocalTime localNow = DateUtil.now().toLocalTime();

        if (responseLastCapturedDate == null) {
            return false;
        } else if (responseLastCapturedDate.equals(today)) {
            return true;
        }

        return responseLastCapturedDate.equals(yesterday) && new Time(localNow.getHourOfDay(), localNow.getMinuteOfHour()).isBefore(dosageTime);
    }

    @Ignore
    public LocalDate getStartDate() {
        List<Medicine> sortedList = new ArrayList<>(medicines);
        Collections.sort(sortedList, new Comparator<Medicine>() {
            @Override
            public int compare(Medicine o1, Medicine o2) {
                return o1.getStartDate().compareTo(o2.getStartDate());
            }
        });
        return sortedList.isEmpty() ? null : sortedList.get(0).getStartDate();
    }

    @Ignore
    public LocalDate getEndDate() {
        Set<Medicine> medicinesWithNonNullEndDate = getMedicinesWithNonNullEndDate();
        if (medicinesWithNonNullEndDate.isEmpty()) {
            return null;
        }

        List<Medicine> sortedList = new ArrayList<>(medicinesWithNonNullEndDate);
        Collections.sort(sortedList, new Comparator<Medicine>() {
            @Override
            public int compare(Medicine o1, Medicine o2) {
                return o2.getEndDate().compareTo(o1.getEndDate());
            }
        });
        return sortedList.isEmpty() ? null : sortedList.get(0).getEndDate();
    }

    @Ignore
    private Set<Medicine> getMedicinesWithNonNullEndDate() {
        Set<Medicine> medicinesWithNonNullEndDate = new HashSet<>();
        for (Medicine medicine : medicines) {
            if (medicine.getEndDate() != null) {
                medicinesWithNonNullEndDate.add(medicine);
            }
        }
        return medicinesWithNonNullEndDate;
    }

    public void validate() {
        for (Medicine medicine : getMedicines()) {
            medicine.validate();
        }
    }

    public DateTime todaysDosageTime() {
        return DateUtil.now().withHourOfDay(dosageTime.getHour()).withMinuteOfHour(dosageTime.getMinute());
    }
}
