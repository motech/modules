package org.motechproject.adherence.domain;

import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.DateTime;
import org.motechproject.model.MotechBaseDataObject;

@TypeDiscriminator("doc.type === 'AdherenceAuditLog'")
public class AdherenceAuditLog extends MotechBaseDataObject {

    @JsonProperty
    private String user;
    @JsonProperty
    private String source;
    @JsonProperty
    private String adherenceLogDocId;
    @JsonProperty
    private int status;
    @JsonProperty
    private DateTime dateModified;

    //Required for ektorp
    public AdherenceAuditLog(){
        super();
    }

    public AdherenceAuditLog(String user, String source, String adherenceLogDocId, int status, DateTime dateModified) {
        super();
        this.user = user;
        this.source = source;
        this.adherenceLogDocId = adherenceLogDocId;
        this.status = status;
        this.dateModified = dateModified;
    }

    public String user() {
        return user;
    }

    public String source() {
        return source;
    }

    public String adherenceLogDocId() {
        return adherenceLogDocId;
    }

    public int status() {
        return status;
    }

    public DateTime dateModified() {
        return dateModified;
    }
}
