package org.motechproject.openmrs19.config;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.motechproject.commons.api.MotechException;
import org.springframework.web.util.UriTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents configuration for a single OpenMRS server.
 */
public class Config {

    private static final String SERVICE_PATH = "/ws/rest/v1";

    private String name;

    private String openMrsUrl;

    private String username;

    private String password;

    private String motechPatientIdentifierTypeName;

    private List<String> patientIdentifierTypeNames;

    /**
     * Creates an URI that points to the resource under the given path on the OpenMRS server.
     *
     * @param path  the path to the resource
     * @return the URI pointing to the resource on the OpenMRS server
     */
    public URI toInstancePath(String path) {
        try {
            return new URI(getBaseApiUrl() + path);
        } catch (URISyntaxException e) {
            throw new MotechException("Bad URI", e);
        }
    }

    /**
     * Creates an URI that points to the resource under the given path on the OpenMRS server. The given template path
     * is first parsed and then given parameters are applied to it resulting in a complete URI.
     *
     * @param path  the path template
     * @param params  the parameters that should be included in the URI
     * @return the URI pointing to the resource on the OpenMRS server
     */
    public URI toInstancePathWithParams(String path, Object... params) {
        return new UriTemplate(getBaseApiUrl() + path).expand(params);
    }

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

    public String getMotechPatientIdentifierTypeName() {
        return motechPatientIdentifierTypeName;
    }

    public void setMotechPatientIdentifierTypeName(String motechPatientIdentifierTypeName) {
        this.motechPatientIdentifierTypeName = motechPatientIdentifierTypeName;
    }

    public List<String> getPatientIdentifierTypeNames() {
        if (patientIdentifierTypeNames == null) {
            patientIdentifierTypeNames = new ArrayList<>();
        }
        return patientIdentifierTypeNames;
    }

    public void setPatientIdentifierTypeNames(List<String> patientIdentifierTypeNames) {
        this.patientIdentifierTypeNames = patientIdentifierTypeNames;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Config)) {
            return false;
        }

        Config other = (Config) o;

        return ObjectUtils.equals(name, other.name) && ObjectUtils.equals(openMrsUrl, other.openMrsUrl)
                && ObjectUtils.equals(username, other.username) && ObjectUtils.equals(password, other.password)
                && ObjectUtils.equals(motechPatientIdentifierTypeName, other.motechPatientIdentifierTypeName)
                && ObjectUtils.equals(patientIdentifierTypeNames, other.patientIdentifierTypeNames);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).append(openMrsUrl).append(username).append(password)
                .append(motechPatientIdentifierTypeName).append(patientIdentifierTypeNames).toHashCode();
    }

    private String getBaseApiUrl() {
        return openMrsUrl + SERVICE_PATH;
    }
}
