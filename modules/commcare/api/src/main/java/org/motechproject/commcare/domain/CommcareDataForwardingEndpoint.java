package org.motechproject.commcare.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * A domain class that represents the information and properties of a data
 * forwarding endpoint in CommCareHQ.
 */
public class CommcareDataForwardingEndpoint {
    @Expose
    private String domain;

    private String id;
    @SerializedName("resource_uri")
    private String resourceUri;

    @Expose
    private String type;
    @Expose
    private String url;

    @Expose
    private String version;

    public CommcareDataForwardingEndpoint() {
        this(null, null, null, null);
    }

    public CommcareDataForwardingEndpoint(String domain, String type, String url, String version) {
        this.domain = domain;
        this.type = type;
        this.url = url;
        this.version = version;

        this.id = null;
        this.resourceUri = null;
    }

    public String getDomain() {
        return this.domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResourceUri() {
        return this.resourceUri;
    }

    public void setResourceUri(String resourceUri) {
        this.resourceUri = resourceUri;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
