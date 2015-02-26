package org.motechproject.csd.domain;

public enum DayOfTheWeek {
    SUNDAY(0),
    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6);

    private final int value;

    private DayOfTheWeek(final int newValue) {
        value = newValue;
    }

    public int getValue() {
        return value;
    }
}
