package org.motechproject.scheduletracking.domain;

import org.joda.time.Period;
import org.motechproject.mds.annotations.Entity;

@Entity
public class Alert {

    private Period offset;
    private Period interval;
    private int count;
    private int index;
    private boolean floating;

    public Alert() {
    }

    public Alert(Period offset, Period interval, int count, int index, boolean floating) {
        this.offset = offset;
        this.interval = interval;
        this.count = count;
        this.index = index;
        this.floating = floating;
    }

    public int getCount() {
        return count;
    }

    public Period getOffset() {
        return offset;
    }

    public Period getInterval() {
        return interval;
    }

    public int getIndex() {
        return index;
    }

    public boolean isFloating() {
        return floating;
    }

    public void setOffset(Period offset) {
        this.offset = offset;
    }

    public void setFloating(boolean floating) {
        this.floating = floating;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setInterval(Period interval) {
        this.interval = interval;
    }
}
