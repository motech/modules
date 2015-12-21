package org.motechproject.messagecampaign.web.model;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.joda.time.LocalDate;
import org.motechproject.commons.date.model.Time;
import org.motechproject.messagecampaign.web.util.LocalDateSerializer;
import org.motechproject.messagecampaign.web.util.TimeSerializer;

import java.io.Serializable;

/**
 * DTO representation of the enrollment in creation. It is used to pass the representation
 * between the view and controller layers during creating and updating of the enrollments.
 */
public class EnrollmentRequest implements Serializable {

    private static final long serialVersionUID = 8082316095036755730L;

    /**
     * ID of the enrollment.
     */
    @JsonProperty
    private Long enrollmentId;

    /**
     * The preferred time to send the messages.
     */
    @JsonProperty
    @JsonSerialize(using = TimeSerializer.class)
    private Time startTime;

    /**
     * The reference date, used as a start point to send messages.
     */
    @JsonProperty
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate referenceDate;

    public EnrollmentRequest(Long enrollmentId, Time startTime, LocalDate referenceDate) {
        this.enrollmentId = enrollmentId;
        this.startTime = startTime;
        this.referenceDate = referenceDate;
    }

    public EnrollmentRequest() {
    }

    public Long getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Long enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public LocalDate getReferenceDate() {
        return referenceDate;
    }

    public void setReferenceDate(LocalDate referenceDate) {
        this.referenceDate = referenceDate;
    }

}
