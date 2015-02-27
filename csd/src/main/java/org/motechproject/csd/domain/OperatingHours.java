package org.motechproject.csd.domain;

import org.joda.time.DateTime;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import java.util.List;

@Entity
public class OperatingHours {

    @Field(required = true)
    private boolean openFlag;

    @Field
    private List<DayOfTheWeek> dayOfTheWeek;

    @Field
    private DateTime beginningHour;

    @Field
    private DateTime endingHour;

    @Field(required = true)
    private DateTime beginEffectiveDate;

    @Field
    private DateTime endEffectiveDate;

    public OperatingHours() {
    }

    public OperatingHours(boolean openFlag, DateTime beginEffectiveDate) {
        this.openFlag = openFlag;
        this.beginEffectiveDate = beginEffectiveDate;
    }

    public OperatingHours(boolean openFlag, List<DayOfTheWeek> dayOfTheWeek, DateTime beginningHour, DateTime endingHour, DateTime beginEffectiveDate, DateTime endEffectiveDate) {
        this.openFlag = openFlag;
        this.dayOfTheWeek = dayOfTheWeek;
        this.beginningHour = beginningHour;
        this.endingHour = endingHour;
        this.beginEffectiveDate = beginEffectiveDate;
        this.endEffectiveDate = endEffectiveDate;
    }

    public boolean isOpenFlag() {
        return openFlag;
    }

    public void setOpenFlag(boolean openFlag) {
        this.openFlag = openFlag;
    }

    public List<DayOfTheWeek> getDayOfTheWeek() {
        return dayOfTheWeek;
    }

    public void setDayOfTheWeek(List<DayOfTheWeek> dayOfTheWeek) {
        this.dayOfTheWeek = dayOfTheWeek;
    }

    public DateTime getBeginningHour() {
        return beginningHour;
    }

    public void setBeginningHour(DateTime beginningHour) {
        this.beginningHour = beginningHour;
    }

    public DateTime getEndingHour() {
        return endingHour;
    }

    public void setEndingHour(DateTime endingHour) {
        this.endingHour = endingHour;
    }

    public DateTime getBeginEffectiveDate() {
        return beginEffectiveDate;
    }

    public void setBeginEffectiveDate(DateTime beginEffectiveDate) {
        this.beginEffectiveDate = beginEffectiveDate;
    }

    public DateTime getEndEffectiveDate() {
        return endEffectiveDate;
    }

    public void setEndEffectiveDate(DateTime endEffectiveDate) {
        this.endEffectiveDate = endEffectiveDate;
    }

    @Override //NO CHECKSTYLE CyclomaticComplexity
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OperatingHours that = (OperatingHours) o;

        if (openFlag != that.openFlag) {
            return false;
        }
        if (!beginEffectiveDate.equals(that.beginEffectiveDate)) {
            return false;
        }
        if (beginningHour != null ? !beginningHour.equals(that.beginningHour) : that.beginningHour != null) {
            return false;
        }
        if (dayOfTheWeek != null ? !dayOfTheWeek.equals(that.dayOfTheWeek) : that.dayOfTheWeek != null) {
            return false;
        }
        if (endEffectiveDate != null ? !endEffectiveDate.equals(that.endEffectiveDate) : that.endEffectiveDate != null) {
            return false;
        }
        if (endingHour != null ? !endingHour.equals(that.endingHour) : that.endingHour != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = (openFlag ? 1 : 0);
        result = 31 * result + (dayOfTheWeek != null ? dayOfTheWeek.hashCode() : 0);
        result = 31 * result + (beginningHour != null ? beginningHour.hashCode() : 0);
        result = 31 * result + (endingHour != null ? endingHour.hashCode() : 0);
        result = 31 * result + beginEffectiveDate.hashCode();
        result = 31 * result + (endEffectiveDate != null ? endEffectiveDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OperatingHours{" +
                "openFlag=" + openFlag +
                ", dayOfTheWeek=" + dayOfTheWeek +
                ", beginningHour=" + beginningHour +
                ", endingHour=" + endingHour +
                ", beginEffectiveDate=" + beginEffectiveDate +
                ", endEffectiveDate=" + endEffectiveDate +
                '}';
    }
}
