package org.motechproject.hub.model;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Class which stores the base url of the hub. Hub is responsible of
 * notifying subscribers of changes in topics to which they subscribed.
 */
@JsonAutoDetect(JsonMethod.NONE)
public class HubSettings {

    @JsonProperty
    private String hubBaseUrl;

    /**
     * Gets the hub base url.
     *
     * @return the hub base url
     */
    public String getHubBaseUrl() {
        return hubBaseUrl;
    }

    /**
     * Sets the hub base url.
     *
     * @param hubBaseUrl the hub base url to be set
     */
    public void setHubBaseUrl(String hubBaseUrl) {
        this.hubBaseUrl = hubBaseUrl;
    }

}
