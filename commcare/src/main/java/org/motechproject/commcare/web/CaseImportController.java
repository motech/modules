package org.motechproject.commcare.web;

import org.motechproject.commcare.pull.CommcareCaseImporter;
import org.motechproject.commcare.pull.CommcareCaseImporterFactory;
import org.motechproject.commcare.pull.CaseImportStatus;
import org.motechproject.commcare.web.domain.CaseImportRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpSession;

import static org.motechproject.commcare.util.Constants.HAS_MANAGE_COMMCARE_PERMISSION;
/**
 * This controller is responsible for importing cases.
 * Can initialize and start importing the cases and check the status of an ongoing import.
 */
@Controller
@RequestMapping("/case-import")
@PreAuthorize(HAS_MANAGE_COMMCARE_PERMISSION)
public class CaseImportController extends CommcareController{
    private static final Logger LOGGER = LoggerFactory.getLogger(CaseImportController.class);

    @Autowired
    private CommcareCaseImporterFactory caseImporterFactory;

    /**
     * Checks the status of the ongoing case import. If there is no import in progress currently,
     * that will be reflected in the returned object.
     * @return the status representation for case import
     */
    @RequestMapping(value = "/status", method = RequestMethod.GET)
    @ResponseBody
    public CaseImportStatus checkImportStatus(HttpSession session) {
        LOGGER.debug("Received import STATUS request");
        CommcareCaseImporter importer = caseImporterFactory.getImporter(session);
        return importer.importStatus();
    }

    /**
     * Initializes case import, on the UI this will trigger a modal with the number of cases.
     * Under the covers this just does a case count for the provided request.
     * @param importRequest the request for
     * @return the number of cases that match this request
     */
    @RequestMapping(value = "/init", method = RequestMethod.POST)
    @ResponseBody
    public int initImport(@RequestBody CaseImportRequest importRequest, HttpSession session) {
        LOGGER.debug("Received import INIT request: {}", importRequest);
        CommcareCaseImporter importer = caseImporterFactory.getImporter(session);
        if (importRequest.getCaseId() == null) {
            return importer.countForImport(importRequest.getDateRange(), importRequest.getConfig());
        } else {
            return importer.checkCaseIdForImport(importRequest.getCaseId(), importRequest.getConfig()) ? 1 : 0;
        }
    }

    /**
     * Starts a case import using the provided request.
     * @param importRequest the request for the case import
     * @return returns whether case imported by id is failed or not.
     */
    @RequestMapping(value = "/start", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void startImport(@RequestBody CaseImportRequest importRequest, HttpSession session) {
        LOGGER.debug("Received import START request: {}", importRequest);
        CommcareCaseImporter importer = caseImporterFactory.getImporter(session);
        importer.startImport(importRequest.getDateRange(), importRequest.getConfig());
    }

    /**
     * Starts a case import by the given case id.
     * @param importRequest the request for the case import
     * @return returns 0 if there is an error and 1 if there is no error.
     */
    @RequestMapping(value = "/import-by-id", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void startImportById(@RequestBody CaseImportRequest importRequest, HttpSession session) {
        LOGGER.debug("Received import START request: {}", importRequest);
        CommcareCaseImporter importer = caseImporterFactory.getImporter(session);
        if (importRequest.getCaseId() != null) {
            importer.importSingleCase(importRequest.getCaseId(), importRequest.getConfig());
        }
    }
}
