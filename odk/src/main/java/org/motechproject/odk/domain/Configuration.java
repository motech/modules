package org.motechproject.odk.domain;


/**
 * Domain entity representation an ODK module user configuration.
 */
public class Configuration {
    private String url;
    private String username;
    private String password;
    private String name;
    private ConfigurationType type;
    private boolean verified;


    public Configuration(String url, String username, String password, String name, ConfigurationType type, boolean verified) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.name = name;
        this.type = type;
        this.verified = verified;
    }

    public Configuration() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ConfigurationType getType() {
        return type;
    }

    public void setType(ConfigurationType type) {
        this.type = type;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
