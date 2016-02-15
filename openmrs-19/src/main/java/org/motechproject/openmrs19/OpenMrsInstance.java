package org.motechproject.openmrs19;

import org.apache.commons.lang.StringUtils;
import org.motechproject.commons.api.MotechException;
import org.motechproject.server.config.SettingsFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriTemplate;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a single OpenMRS Web Application instance.
 */
@Component
public class OpenMrsInstance {

    private static final String OPENMRS_IDENTIFIER_TYPES_PROPERTY = "openmrs.identifierTypes";
    private static final String OPENMRS_MOTECH_ID_NAME_PROPERTY = "openmrs.motechIdName";
    private static final String OPENMRS_URL_PROPERTY = "openmrs.url";
    private static final String OPENMRS_WEB_SERVICE_PATH = "/ws/rest/v1";

    private String openmrsUrl;
    private String motechPatientIdentifierTypeName;
    private List<String> patientIdentifierTypesNames;

    private SettingsFacade settingsFacade;

    @Autowired
    public OpenMrsInstance(@Qualifier("openMrs19Settings") SettingsFacade settingsFacade) {
        this.settingsFacade = settingsFacade;
    }

    @PostConstruct
    public void readSettings() {
        this.openmrsUrl = settingsFacade.getProperty(OPENMRS_URL_PROPERTY) + OPENMRS_WEB_SERVICE_PATH;
        this.motechPatientIdentifierTypeName = settingsFacade.getProperty(OPENMRS_MOTECH_ID_NAME_PROPERTY);
        this.patientIdentifierTypesNames = parseIdentifierTypesProperty();
    }

    /**
     * Creates an URI that points to the resource under the given path on the OpenMRS server.
     *
     * @param path  the path to the resource
     * @return the URI pointing to the resource on the OpenMRS server
     */
    public URI toInstancePath(String path) {
        try {
            return new URI(openmrsUrl + path);
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
        return new UriTemplate(openmrsUrl + path).expand(params);
    }

    public String getOpenmrsUrl() {
        return openmrsUrl;
    }

    public String getMotechPatientIdentifierTypeName() {
        return motechPatientIdentifierTypeName;
    }

    public List<String> getPatientIdentifierTypesNames() {
        return patientIdentifierTypesNames;
    }

    private List<String> parseIdentifierTypesProperty() {
        String property = settingsFacade.getProperty(OPENMRS_IDENTIFIER_TYPES_PROPERTY);

        return StringUtils.isEmpty(property) ? new ArrayList<>() : Arrays.asList(property.split(","));
    }
}
