package org.motechproject.commcare.domain;

import com.google.gson.annotations.SerializedName;
import org.motechproject.commons.api.model.MotechProperties;

import java.util.List;
import java.util.Map;

/**
 * Represents a single CommCareHQ case. A case can be literally anything that one would want to track and monitor over
 * time. It's a part of the CommCareHQ model.
 */
public class CaseJson {

    @SerializedName("dated_closed")
    private String dateClosed;

    @SerializedName("domain")
    private String domain;

    @SerializedName("xform_ids")
    private List<String> xformIds;

    @SerializedName("version")
    private String version;

    @SerializedName("server_date_opened")
    private String serverDateOpened;

    @SerializedName("properties")
    private MotechProperties caseProperties;

    @SerializedName("server_date_modified")
    private String serverDateModified;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("date_modified")
    private String dateModified;

    @SerializedName("case_id")
    private String caseId;

    @SerializedName("closed")
    private boolean closed;

    @SerializedName("indices")
    private Map<String, Map<String, String>> indices;

    public boolean isClosed() {
        return closed;
    }

    public Map<String, Map<String, String>> getIndices() {
        return indices;
    }

    public String getCaseId() {
        return caseId;
    }

    public String getDateModified() {
        return dateModified;
    }

    public String getUserId() {
        return userId;
    }

    public MotechProperties getCaseProperties() {
        return caseProperties;
    }

    public String getServerDateModified() {
        return serverDateModified;
    }

    public String getServerDateOpened() {
        return serverDateOpened;
    }

    public String getVersion() {
        return version;
    }

    public String getDomain() {
        return domain;
    }

    public List<String> getXformIds() {
        return xformIds;
    }

    public String getDateClosed() {
        return dateClosed;
    }

    public void setDateClosed(String dateClosed) {
        this.dateClosed = dateClosed;
    }

    public void setIndices(Map<String, Map<String, String>> indices) {
        this.indices = indices;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public void setCaseProperties(MotechProperties caseProperties) {
        this.caseProperties = caseProperties;
    }

    public void setServerDateOpened(String serverDateOpened) {
        this.serverDateOpened = serverDateOpened;
    }

    public void setServerDateModified(String serverDateModified) {
        this.serverDateModified = serverDateModified;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setXformIds(List<String> xformIds) {
        this.xformIds = xformIds;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
