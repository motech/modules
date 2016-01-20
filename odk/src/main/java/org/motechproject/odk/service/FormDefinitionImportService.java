package org.motechproject.odk.service;


import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.ImportStatus;

/**
 * A service for importing form definitions from external applications.
 */
public interface FormDefinitionImportService {

    /**
     * Imports form definitions from an external application.
     * @param config {@link Configuration}
     * @return {@link ImportStatus} true if successful; false otherwise.
     */
    ImportStatus importForms(Configuration config);
}
