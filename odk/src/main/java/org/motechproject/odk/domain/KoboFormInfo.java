package org.motechproject.odk.domain;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Contains the url for a particular KoboToolBox form.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class KoboFormInfo {

    public KoboFormInfo() {
    }

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
