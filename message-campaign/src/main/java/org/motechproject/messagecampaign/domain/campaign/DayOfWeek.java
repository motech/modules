package org.motechproject.messagecampaign.domain.campaign;

/**
 * Represents a day of a week. Used to define deliver dates for the
 * {@link DayOfWeekCampaign}.
 */
public enum DayOfWeek {
    Monday(1),
    Tuesday(2),
    Wednesday(3),
    Thursday(4),
    Friday(5),
    Saturday(6),
    Sunday(7);

    private final Integer value;

    private DayOfWeek(Integer value) {
        this.value = value;
    }

    /**
     * @return numerical representation of a day in the week,
     * starting with 1, being Monday and ending with 7, being Sunday.
     */
    public Integer getValue() {
        return value;
    }
}
