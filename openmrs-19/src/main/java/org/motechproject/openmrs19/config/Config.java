package org.motechproject.openmrs19.config;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Represents configuration for a single OpenMRS server.
 */
public class Config {

    private String name;

    private String openMrsUrl;

    private String username;

    private String password;

    private String motechId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpenMrsUrl() {
        return openMrsUrl;
    }

    public void setOpenMrsUrl(String openMrsUrl) {
        this.openMrsUrl = openMrsUrl;
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

    public String getMotechId() {
        return motechId;
    }

    public void setMotechId(String motechId) {
        this.motechId = motechId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Config)) {
            return false;
        }

        Config other = (Config) o;

        return StringUtils.equals(name, other.name) && StringUtils.equals(openMrsUrl, other.openMrsUrl)
                && StringUtils.equals(username, other.username) && StringUtils.equals(password, other.password)
                && StringUtils.equals(motechId, other.motechId);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).append(openMrsUrl).append(username).append(password).append(motechId)
                .toHashCode();
    }
}
