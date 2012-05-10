package org.motechproject.adherence.domain;

import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.model.MotechBaseDataObject;
import org.motechproject.util.DateUtil;

@TypeDiscriminator("doc.type === 'AdherenceAuditLog'")
public class AdherenceAuditLog extends MotechBaseDataObject {

    @JsonProperty
    private String user;
    @JsonProperty
    private String source;
    @JsonProperty
    private String externalId;
    @JsonProperty
    private String treatmentId;
    @JsonProperty
    private LocalDate doseDate;
    @JsonProperty
    private int status;
    @JsonProperty
    private DateTime dateModified;

    //Required for ektorp
    public AdherenceAuditLog(){
        super();
    }

    public AdherenceAuditLog(AdherenceLog adherenceLog, String user, String source) {
        super();
        this.user = user;
        this.source = source;
        this.externalId = adherenceLog.externalId();
        this.treatmentId = adherenceLog.treatmentId();
        this.status = adherenceLog.status();
        this.doseDate = adherenceLog.doseDate();
        this.dateModified = DateUtil.now();
    }

    public String user() {
        return user;
    }

    public String source() {
        return source;
    }

    public int status() {
        return status;
    }

    public DateTime dateModified() {
        return DateUtil.setTimeZone(dateModified);
    }

    public String externalId() {
        return externalId;
    }

    public String treatmentId() {
        return treatmentId;
    }

    public LocalDate doseDate() {
        return doseDate;
    }
}
