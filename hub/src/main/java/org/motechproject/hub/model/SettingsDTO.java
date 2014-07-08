package org.motechproject.hub.model;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonAutoDetect(JsonMethod.NONE)
public class SettingsDTO {

    @JsonProperty
    private HubSettings settings;

    public SettingsDTO() {
        this.settings = new HubSettings();
    }

    public String getHubBaseUrl() {
        return settings.getHubBaseUrl();
    }

    public void setHubBaseUrl(String hubBaseUrl) {
        this.settings.setHubBaseUrl(hubBaseUrl);
    }

}
