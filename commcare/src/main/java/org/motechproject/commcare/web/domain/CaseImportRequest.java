package org.motechproject.commcare.web.domain;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.DateTime;
import org.motechproject.commons.api.Range;
import org.motechproject.commons.date.util.DateUtil;

import java.io.Serializable;

/**
 * Represents a request for importing cases
 */
public class CaseImportRequest implements Serializable{
    private static final long serialVersionUID = 4392180915357501643L;

    /**
     * To get cases modified after this date.
     */
    private DateTime modifiedDateStart;

    /**
     * To get cases modified before this date.
     */
    private DateTime modifiedDateEnd;

    /**
     * The name of the configuration for which to initiate the import.
     */
    private String config;

    /**
     * The uuid of the case to be imported.
     */
    private String caseId;
    /**
     * @return date after which cases are modified.
     */
    public DateTime getModifiedDateStart() {
        return modifiedDateStart;
    }

    /**
     * @param modifiedDateStart date after which cases are modified.
     */
    public void setModifiedDateStart(DateTime modifiedDateStart) {
        this.modifiedDateStart = modifiedDateStart;
    }

    /**
     * @return date before which cases are modified.
     */
    public DateTime getModifiedDateEnd() {
        return modifiedDateEnd;
    }

    /**
     * @param modifiedDateEnd date before which cases are modified.
     */
    public void setModifiedDateEnd(DateTime modifiedDateEnd) {
        this.modifiedDateEnd = modifiedDateEnd;
    }

    /**
     * @return The name of the configuration for which to initiate the import.
     */
    public String getConfig() {
        return config;
    }

    /**
     * @param config The name of the configuration for which to initiate the import.
     */
    public void setConfig(String config) {
        this.config = config;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    @JsonIgnore
    public Range<DateTime> getDateRange() {
        return new Range<>(DateUtil.setTimeZoneUTC(modifiedDateStart),
                DateUtil.setTimeZoneUTC(modifiedDateEnd));
    }
}
