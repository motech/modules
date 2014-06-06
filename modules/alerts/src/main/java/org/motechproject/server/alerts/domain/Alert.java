package org.motechproject.server.alerts.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.joda.time.DateTime;
import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.Ignore;
import org.motechproject.mds.annotations.UIDisplayable;

import java.util.HashMap;
import java.util.Map;

@Entity
public class Alert implements Comparable<Alert> {
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
    private String description;

    @Field
    private Map<String, String> data;

    public Alert() {
        this(null, null, null, AlertType.MEDIUM, AlertStatus.NEW, 0, null);
    }

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

    public Alert(String externalId, String name, String description, AlertType alertType,
                 AlertStatus status, int priority, Map<String, String> data) {
        this(externalId, alertType, status, priority, data);
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AlertType getAlertType() {
        return alertType;
    }

    public void setAlertType(AlertType alertType) {
        this.alertType = alertType;
    }

    @Ignore
    public long getDateTimeInMillis() {
        return getDateTime().getMillis();
    }

    public DateTime getDateTime() {
        return DateUtil.setTimeZoneUTC(dateTime);
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public AlertStatus getStatus() {
        return status;
    }

    public void setStatus(AlertStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, String> getData() {
        return data;
    }

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
