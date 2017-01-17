package org.motechproject.commcare.web.domain;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.DateTime;
import org.motechproject.commons.api.Range;
import org.motechproject.commons.date.util.DateUtil;

import java.io.Serializable;

/**
 * Represents a request for form import.
 */
public class FormImportRequest implements Serializable {

    private static final long serialVersionUID = -3930771991256898793L;

    /**
     * The starting date for form import.
     */
    private DateTime receivedOnStart;

    /**
     * The end date for form import.
     */
    private DateTime receivedOnEnd;

    /**
     * The id for form import.
     */
    private String formId;

    /**
     * The name of the configuration for which to initiate the import.
     */
    private String config;

    /**
     * @return the starting date for form import
     */
    public DateTime getReceivedOnStart() {
        return receivedOnStart;
    }

    /**
     * @param receivedOnStart the starting date for form import
     */
    public void setReceivedOnStart(DateTime receivedOnStart) {
        this.receivedOnStart = receivedOnStart;
    }

    /**
     * @return the end date for form import
     */
    public DateTime getReceivedOnEnd() {
        return receivedOnEnd;
    }

    /**
     * @param receivedOnEnd the end date for form import
     */
    public void setReceivedOnEnd(DateTime receivedOnEnd) {
        this.receivedOnEnd = receivedOnEnd;
    }

    /**
     * @return the form id for form import
     */
    public String getFormId() {
        return formId;
    }

    /**
     * @param formId the id for form import
     */
    public void setFormId(String formId) {
        this.formId = formId;
    }

    /**
     * @return the name of the configuration for which to initiate the import
     */
    public String getConfig() {
        return config;
    }

    /**
     * @param config the name of the configuration for which to initiate the import
     */
    public void setConfig(String config) {
        this.config = config;
    }


    @JsonIgnore
    public Range<DateTime> getDateRange() {
        return new Range<>(DateUtil.setTimeZoneUTC(receivedOnStart),
                DateUtil.setTimeZoneUTC(receivedOnEnd));
    }

    @Override
    public String toString() {
        return "FormImportRequest{" +
                "receivedOnStart=" + receivedOnStart +
                ", receivedOnEnd=" + receivedOnEnd +
                ", config='" + config + '\'' +
                '}';
    }
}
