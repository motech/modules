package org.motechproject.hub.model;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Class which stores base url of the hub. Hub is responsible of
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

    /**
     * For now only checks if base url is not blank.
     * This is not used at the moment.
     *
     * @return false if the hub base url is empty, null or whitespace only, true otherwise
     */
    public boolean canMakeConnection() {
        return StringUtils.isNotBlank(hubBaseUrl);
    }
}
