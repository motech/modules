package org.motechproject.dhis2.service;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * DHIS2 server configuration details.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Settings {
    private String serverURI;
    private String username;
    private String password;

    public Settings() { }

    public Settings(String serverURI, String username, String password) {
        this.serverURI = serverURI;
        this.username = username;
        this.password = password;
    }

    public String getServerURI() {
        return serverURI;
    }

    public void setServerURI(String serverURI) {
        this.serverURI = serverURI;
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
}
