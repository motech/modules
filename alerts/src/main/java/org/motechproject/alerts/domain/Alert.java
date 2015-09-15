package org.motechproject.alerts.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.joda.time.DateTime;
import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.Ignore;
import org.motechproject.mds.annotations.UIDisplayable;

import javax.jdo.annotations.Column;
import java.util.HashMap;
import java.util.Map;

/**
 * The alert class, the core of this module. Note that the alert module
 * does not impose any interpretations on the fields of this class, that is left
 * to the implementation that will make use of this module.
 */
@Entity
public class Alert implements Comparable<Alert> {

    @Field
    private Long id;

    @Field(required = true)
    private String externalId;

    @UIDisplayable(position = 0)
    private String name;

    @Field(required = true, defaultValue = "MEDIUM")
    @UIDisplayable(position = 1)
    private AlertType alertType;

    @UIDisplayable(position = 4)
    private DateTime dateTime;

    @Field(required = true, defaultValue = "0")
    @UIDisplayable(position = 3)
    private Long priority;

    @Field(required = true, defaultValue = "NEW")
    @UIDisplayable(position = 2)
    private AlertStatus status;

    @UIDisplayable(position = 5)
    @Column(length = 5000)
    private String description;

    @Field
    private Map<String, String> data;

    /**
     * Constructs a new alert with medium type, new status and 0 priority.
     */
    public Alert() {
        this(null, null, null, AlertType.MEDIUM, AlertStatus.NEW, 0, null);
    }

    /**
     * Creates a new instance of the alert without the name and description.
     * @param externalId the external id of this alert
     * @param alertType the type of this alert
     * @param status the status of this alert
     * @param priority the number representing the alert priority
     * @param data the additional data for this alert
     */
    public Alert(String externalId, AlertType alertType, AlertStatus status, int priority,
                 Map<String, String> data) {
        this.externalId = externalId;
        this.alertType = alertType;
        this.status = status;
        this.priority = (long) priority;
        this.dateTime = DateUtil.now();
        this.data = new HashMap<>();

        if (null != data) {
            this.data.putAll(data);
        }
    }

    /**
     * Creates a new instance of an alert.
     * @param externalId the external id of this alert
     * @param name the short name of this alert
     * @param description the description of this alert
     * @param alertType the type of this alert
     * @param status the status of this alert
     * @param priority the number representing the alert priority
     * @param data the additional data for this alert
     */
    public Alert(String externalId, String name, String description, AlertType alertType,
                 AlertStatus status, int priority, Map<String, String> data) {
        this(externalId, alertType, status, priority, data);
        this.name = name;
        this.description = description;
    }

    /**
     * @return the id of this alert (auto-generated field)
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id of this alert (auto-generated field)
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the external id for this alert. The external id can be used for associating this alert
     * with different external concepts, such as patients, doctors, etc.
     * @return the external id of this alert
     */
    public String getExternalId() {
        return externalId;
    }

    /**
     * Sets the external id for this alert. The external id can be used for associating this alert
     * with different external concepts, such as patients, doctors, etc.
     * @param externalId external id of this alert
     */
    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    /**
     * Returns the name of this alert. Can be used for giving alerts short, human-friendly descriptions.
     * @return the name of this alert
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this alert. Can be used for giving alerts short, human-friendly descriptions.
     * @param name the name of this alert
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the type of this alert. The type describes the importance of this alert.
     * @return the type of this alert
     */
    public AlertType getAlertType() {
        return alertType;
    }

    /**
     * Sets the type of this alert. The type describes the importance of this alert.
     * @param alertType the type of this alert
     */
    public void setAlertType(AlertType alertType) {
        this.alertType = alertType;
    }

    /**
     * Returns the value of {@link #getDateTime()} as milliseconds from 1970-01-01T00:00:00Z.
     * @return the datetime of this alert in milliseconds
     */
    @Ignore
    public long getDateTimeInMillis() {
        return getDateTime().getMillis();
    }

    /**
     * Returns the datetime of this alert. Usually describes when the alert should be/was delivered.
     * @return the datetime of this alert
     */
    public DateTime getDateTime() {
        return DateUtil.setTimeZoneUTC(dateTime);
    }

    /**
     * Sets the datetime of this alert. Usually describes when the alert should be/was delivered.
     * @param dateTime  the datetime of this alert
     */
    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * Returns the priority of this value in number format (long). This is independent from the alert type.
     * @return the priority of this alert
     */
    public Long getPriority() {
        return priority;
    }

    /**
     * Sets the priority of this value in number format (long). This is independent from the alert type.
     * @param priority the priority of this alert
     */
    public void setPriority(Long priority) {
        this.priority = priority;
    }

    /**
     * Returns the status of this alert, describing whether it is a new alert, a read alert or a closed alert.
     * The actual meaning of those values is left to the implementation.
     * @return the status of this alert
     */
    public AlertStatus getStatus() {
        return status;
    }

    /**
     * Sets the status of this alert, describing whether it is a new alert, a read alert or a closed alert.
     * The actual meaning of those values is left to the implementation.
     * @param status  the status of this alert
     */
    public void setStatus(AlertStatus status) {
        this.status = status;
    }

    /**
     * @return the description of this alert
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description a description of this alert
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the additional data for this alert. This a string-string map that allows implementations
     * to place additional data in the alert.
     * @return the data map for this alert
     */
    public Map<String, String> getData() {
        return data;
    }

    /**
     * Sets the additional data for this alert. This a string-string map that allows implementations
     * to place additional data in the alert.
     * @param data  the data map for this alert
     */
    public void setData(Map<String, String> data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Alert)) {
            return false;
        }

        Alert alert = (Alert) o;

        return new EqualsBuilder().append(id, alert.id).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).toHashCode();
    }

    @Override
    public String toString() {
        return "Alert{" +
                "id=" + id +
                ", externalId='" + externalId + '\'' +
                ", name='" + name + '\'' +
                ", alertType=" + alertType +
                ", dateTime=" + getDateTime() +
                ", priority=" + priority +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", data=" + data +
                '}';
    }

    @Override
    public int compareTo(Alert o) {
        return Long.compare(getPriority(), o.getPriority());
    }
}
