package org.motechproject.commcare.web;

import org.motechproject.commcare.pull.CommcareFormImporter;
import org.motechproject.commcare.pull.CommcareFormImporterFactory;
import org.motechproject.commcare.pull.FormImportStatus;
import org.motechproject.commcare.web.domain.FormImportRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpSession;

import static org.motechproject.commcare.util.Constants.HAS_MANAGE_COMMCARE_PERMISSION;

/**
 * The controller responsible for tbe form import tab in the UI.
 * Allows starting the form form import and checking the status of an ongoing import.
 */
@Controller
@RequestMapping("/form-import")
@PreAuthorize(HAS_MANAGE_COMMCARE_PERMISSION)
public class FormImportController extends CommcareController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FormImportController.class);

    @Autowired
    private CommcareFormImporterFactory importerFactory;

    /**
     * Checks the status of the ongoing form import. If there is no import in progress currently,
     * that will be reflected in the returned object.
     *
     * @return the status representation for form import
     */
    @RequestMapping(value = "/status", method = RequestMethod.GET)
    @ResponseBody
    public FormImportStatus checkImportStatus(HttpSession session) {
        LOGGER.debug("Received import STATUS request");
        CommcareFormImporter importer = importerFactory.getImporter(session);
        return importer.importStatus();
    }

    /**
     * Initializes form import, on the UI this will trigger a modal with the number of forms.
     * Under the covers this just does a form count for the provided request.
     *
     * @param importRequest the request for
     * @return the number of forms that match this request
     */
    @RequestMapping(value = "/init", method = RequestMethod.POST)
    @ResponseBody
    public int initImport(@RequestBody FormImportRequest importRequest, HttpSession session) {
        LOGGER.debug("Received import INIT request: {}", importRequest);
        CommcareFormImporter importer = importerFactory.getImporter(session);
        if (importRequest.getFormId() == null) {
            return importer.countForImport(importRequest.getDateRange(), importRequest.getConfig());
        } else {
            return importer.checkFormIdForImport(importRequest.getFormId(), importRequest.getConfig()) ? 1 : 0;
        }
    }

    /**
     * Starts a form import using the provided request.
     *
     * @param importRequest the request for the form import
     */
    @RequestMapping(value = "/start", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void startImport(@RequestBody FormImportRequest importRequest, HttpSession session) {
        LOGGER.debug("Received import START request: {}", importRequest);
        CommcareFormImporter importer = importerFactory.getImporter(session);
        if (importRequest.getFormId() == null) {
            importer.startImport(importRequest.getDateRange(), importRequest.getConfig());
        } else {
            importer.startImportById(importRequest.getFormId(), importRequest.getConfig());
        }
    }
}
