package org.motechproject.odk.web;


import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.ImportStatus;
import org.motechproject.odk.exception.ConfigurationTypeException;
import org.motechproject.odk.service.ConfigurationService;
import org.motechproject.odk.service.factory.FormDefinitionImportServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Controller mapped to /import. This controller initiates the form definition import process.
 *
 */
@Controller
public class ImportController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportController.class);

    @Autowired
    private FormDefinitionImportServiceFactory formDefinitionImportServiceFactory;

    @Autowired
    private ConfigurationService configurationService;

    /**
     * Initiates the form definition import process for a configuration identified by the name in the path.
     * @param config The name of the configuration.
     * @return {@link ImportStatus} True if successful; false otherwise.
     */
    @RequestMapping(value = "/import/{config}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasRole('importFormsODK')")
    public ImportStatus syncForms(@PathVariable("config") String config) {
        try {
            Configuration configuration = configurationService.getConfigByName(config);
            return formDefinitionImportServiceFactory.getService(configuration.getType()).importForms(configuration);
        } catch (ConfigurationTypeException e) {
            LOGGER.error(e.toString());
            return new ImportStatus(false);
        }
    }
}
