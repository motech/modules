package org.motechproject.odk.service.factory;

import org.motechproject.odk.domain.ConfigurationType;
import org.motechproject.odk.exception.ConfigurationTypeException;
import org.motechproject.odk.service.FormDefinitionImportService;
import org.motechproject.odk.service.impl.FormDefinitionImportServiceKobo;
import org.motechproject.odk.service.impl.FormDefinitionImportServiceODK;
import org.motechproject.odk.service.impl.FormDefinitionImportServiceOna;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Factory class for {@link FormDefinitionImportService}
 */

@Component
public class FormDefinitionImportServiceFactory {

    private static final String NO_SERVICE = "No form definition import service for configuration type: ";

    @Autowired
    private FormDefinitionImportServiceODK formDefinitionImportServiceODK;

    @Autowired
    private FormDefinitionImportServiceOna formDefinitionImportServiceOna;

    @Autowired
    private FormDefinitionImportServiceKobo formDefinitionImportServiceKobo;


    /**
     * Returns the appropriate {@link FormDefinitionImportService} bean for the configuration type.
     * @param type The {@link ConfigurationType} associated with the form definition.
     * @return {@link FormDefinitionImportService}
     */
    public FormDefinitionImportService getService(ConfigurationType type) throws ConfigurationTypeException {

        switch (type) {
            case ODK:
                return formDefinitionImportServiceODK;

            case ONA:
                return formDefinitionImportServiceOna;

            case KOBO:
                return formDefinitionImportServiceKobo;

            default:
                throw new ConfigurationTypeException(NO_SERVICE + type);
        }
    }
}
