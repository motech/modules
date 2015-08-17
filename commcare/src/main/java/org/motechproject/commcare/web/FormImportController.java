package org.motechproject.commcare.web;

import org.motechproject.commcare.pull.CommcareFormImporter;
import org.motechproject.commcare.pull.FormImportStatus;
import org.motechproject.commcare.web.domain.FormImportRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/form-import")
public class FormImportController extends CommcareController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FormImportController.class);

    @Autowired
    private CommcareFormImporter commcareFormImporter;

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    @ResponseBody
    public FormImportStatus checkImportStatus() {
        LOGGER.debug("Received import STATUS request");
        return commcareFormImporter.importStatus();
    }

    @RequestMapping(value = "/init", method = RequestMethod.POST)
    @ResponseBody
    public long initImport(@RequestBody FormImportRequest importRequest) {
        LOGGER.debug("Received import INIT request: {}", importRequest);
        return commcareFormImporter.countForImport(importRequest.getDateRange(),
                importRequest.getConfig());
    }

    @RequestMapping(value = "/start", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void startImport(@RequestBody FormImportRequest importRequest) {
        LOGGER.debug("Received import START request: {}", importRequest);
        commcareFormImporter.startImport(importRequest.getDateRange(), importRequest.getConfig());
    }
}
