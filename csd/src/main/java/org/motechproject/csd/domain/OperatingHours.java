package org.motechproject.csd.domain;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.motechproject.csd.adapters.DateAdapter;
import org.motechproject.csd.adapters.DayOfTheWeekAdapter;
import org.motechproject.csd.adapters.TimeAdapter;
import org.motechproject.mds.annotations.Cascade;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.UIDisplayable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Java class for operatingHours complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="operatingHours">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="openFlag" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="dayOfTheWeek" type="{http://www.w3.org/2001/XMLSchema}integer" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="beginningHour" type="{http://www.w3.org/2001/XMLSchema}time" minOccurs="0"/>
 *         &lt;element name="endingHour" type="{http://www.w3.org/2001/XMLSchema}time" minOccurs="0"/>
 *         &lt;element name="beginEffectiveDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="endEffectiveDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@Entity
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(propOrder = { "openFlag", "daysOfTheWeek", "beginningHour", "endingHour", "beginEffectiveDate", "endEffectiveDate" })
public class OperatingHours extends AbstractID {

    @UIDisplayable(position = 3)
    @Field(required = true)
    private boolean openFlag;

    @UIDisplayable(position = 0)
    @Field
    @Cascade(delete = true)
    private List<DayOfTheWeek> daysOfTheWeek = new ArrayList<>();

    @UIDisplayable(position = 1)
    @Field(tooltip = "Only time and not date matters in this entity")
    private DateTime beginningHour;

    @UIDisplayable(position = 2)
    @Field(tooltip = "Only time and not date matters in this entity")
    private DateTime endingHour;

    @UIDisplayable(position = 4)
    @Field(required = true, tooltip = "Only date and not time matters in this entity")
    private DateTime beginEffectiveDate;

    @UIDisplayable(position = 5)
    @Field(tooltip = "Only date and not time matters in this entity")
    private DateTime endEffectiveDate;

    public OperatingHours() {
    }

    public OperatingHours(boolean openFlag, DateTime beginEffectiveDate) {
        this.openFlag = openFlag;
        this.beginEffectiveDate = beginEffectiveDate;
    }

    public OperatingHours(boolean openFlag, List<DayOfTheWeek> daysOfTheWeek, DateTime beginningHour, DateTime endingHour, DateTime beginEffectiveDate, DateTime endEffectiveDate) {
        this.openFlag = openFlag;
        this.daysOfTheWeek = daysOfTheWeek;
        this.beginningHour = beginningHour;
        this.endingHour = endingHour;
        this.beginEffectiveDate = beginEffectiveDate;
        this.endEffectiveDate = endEffectiveDate;
    }

    public boolean isOpenFlag() {
        return openFlag;
    }

    @XmlElement(required = true)
    public void setOpenFlag(boolean openFlag) {
        this.openFlag = openFlag;
    }

    public List<DayOfTheWeek> getDaysOfTheWeek() {
        return daysOfTheWeek;
    }

    @XmlElement(name = "dayOfTheWeek")
    @XmlSchemaType(name = "integer")
    @XmlJavaTypeAdapter(type = DayOfTheWeek.class, value = DayOfTheWeekAdapter.class)
    public void setDaysOfTheWeek(List<DayOfTheWeek> daysOfTheWeek) {
        this.daysOfTheWeek = daysOfTheWeek;
    }

    public DateTime getBeginningHour() {
        return beginningHour;
    }

    @XmlElement
    @XmlSchemaType(name = "time")
    @XmlJavaTypeAdapter(type = DateTime.class, value = TimeAdapter.class)
    public void setBeginningHour(DateTime beginningHour) {
        this.beginningHour = beginningHour;
    }

    public DateTime getEndingHour() {
        return endingHour;
    }

    @XmlElement
    @XmlSchemaType(name = "time")
    @XmlJavaTypeAdapter(type = DateTime.class, value = TimeAdapter.class)
    public void setEndingHour(DateTime endingHour) {
        this.endingHour = endingHour;
    }

    public DateTime getBeginEffectiveDate() {
        return beginEffectiveDate;
    }

    @XmlElement(required = true)
    @XmlSchemaType(name = "date")
    @XmlJavaTypeAdapter(type = DateTime.class, value = DateAdapter.class)
    public void setBeginEffectiveDate(DateTime beginEffectiveDate) {
        this.beginEffectiveDate = beginEffectiveDate;
    }

    public DateTime getEndEffectiveDate() {
        return endEffectiveDate;
    }

    @XmlElement
    @XmlSchemaType(name = "date")
    @XmlJavaTypeAdapter(type = DateTime.class, value = DateAdapter.class)
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
        if (!beginEffectiveDate.toLocalDate().isEqual(that.beginEffectiveDate.toLocalDate())) {
            return false;
        }
        if (beginningHour != null ? !beginningHour.toLocalTime().isEqual(that.beginningHour.toLocalTime()) : that.beginningHour != null) {
            return false;
        }
        if (daysOfTheWeek != null ? !daysOfTheWeek.equals(that.daysOfTheWeek) : that.daysOfTheWeek != null) {
            return false;
        }
        if (endEffectiveDate != null ? !endEffectiveDate.toLocalDate().isEqual(that.endEffectiveDate.toLocalDate()) : that.endEffectiveDate != null) {
            return false;
        }
        if (endingHour != null ? !endingHour.toLocalTime().isEqual(that.endingHour.toLocalTime()) : that.endingHour != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = (openFlag ? 1 : 0);
        result = 31 * result + (daysOfTheWeek != null ? daysOfTheWeek.hashCode() : 0);
        result = 31 * result + (beginningHour != null ? beginningHour.hashCode() : 0);
        result = 31 * result + (endingHour != null ? endingHour.hashCode() : 0);
        result = 31 * result + beginEffectiveDate.hashCode();
        result = 31 * result + (endEffectiveDate != null ? endEffectiveDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        String openingHours = "";
        String val = "";
        if (beginningHour != null) {
            openingHours = openingHours + beginningHour.toString(DateTimeFormat.forPattern("HH:mm"));
        }
        openingHours = openingHours + "-";
        if (endingHour != null) {
            openingHours = openingHours + endingHour.toString(DateTimeFormat.forPattern("HH:mm"));
        }
        if (daysOfTheWeek != null && !daysOfTheWeek.isEmpty()) {
            val = val + daysOfTheWeek.toString() + " ";
        }
        if (!"-".equals(openingHours)) {
            val = val + openingHours;
        }
        if (val.isEmpty()) {
            return beginEffectiveDate.toString(DateTimeFormat.forPattern("yyyy-MM-dd"));
        }
        return val;
    }
}
