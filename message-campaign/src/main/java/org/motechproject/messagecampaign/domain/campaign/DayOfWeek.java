package org.motechproject.messagecampaign.domain.campaign;

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

    public Integer getValue() {
        return value;
    }
}
