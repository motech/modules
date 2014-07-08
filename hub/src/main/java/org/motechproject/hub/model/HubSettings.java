package org.motechproject.hub.model;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonAutoDetect(JsonMethod.NONE)
public class HubSettings {

    @JsonProperty
    private String hubBaseUrl;

    public String getHubBaseUrl() {
        return hubBaseUrl;
    }

    public void setHubBaseUrl(String hubBaseUrl) {
        this.hubBaseUrl = hubBaseUrl;
    }

    public boolean canMakeConnection() {
        return StringUtils.isNotBlank(hubBaseUrl);
    }
}
