package org.motechproject.hub.model;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Represent a single hub settings. Used by {@link org.motechproject.hub.web.HubController}.
 */
@JsonAutoDetect(JsonMethod.NONE)
public class SettingsDTO {

    @JsonProperty
    private HubSettings settings;

    /**
     * Creates a new instance of <code>SettingsDTO</code> with new
     * {@link org.motechproject.hub.model.HubSettings} and the hub base url
     * set to null.
     */
    public SettingsDTO() {
        this.settings = new HubSettings();
    }

    /**
     * Gets the hub base url.
     *
     * @return the hub base url as a <code>String</code>
     */
    public String getHubBaseUrl() {
        return settings.getHubBaseUrl();
    }

    /**
     * Sets the hub base url.
     *
     * @param hubBaseUrl the hub base url to be set
     */
    public void setHubBaseUrl(String hubBaseUrl) {
        this.settings.setHubBaseUrl(hubBaseUrl);
    }

}
