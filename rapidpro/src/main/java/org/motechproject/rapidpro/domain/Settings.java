package org.motechproject.rapidpro.domain;

/**
 * Domain object for Rapidpro settings.
 */
public class Settings {

    private String apiKey;
    private String version;

    public Settings() {
    }

    public Settings(String apiKey, String version) {
        this.apiKey = apiKey;
        this.version = version;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
