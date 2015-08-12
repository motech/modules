package org.motechproject.commcare.web;

import org.motechproject.commcare.pull.CommcareFormImporter;
import org.motechproject.commcare.pull.FormImportStatus;
import org.motechproject.commcare.web.domain.FormImportRequest;
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

    @Autowired
    private CommcareFormImporter commcareFormImporter;

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    @ResponseBody
    public FormImportStatus checkImportStatus() {
        return commcareFormImporter.importStatus();
    }

    @RequestMapping(value = "/init", method = RequestMethod.POST)
    @ResponseBody
    public long initImport(@RequestBody FormImportRequest importRequest) {
        return commcareFormImporter.countForImport(importRequest.getDateRange(),
                importRequest.getConfig());
    }

    @RequestMapping(value = "/start", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void startImport(@RequestBody FormImportRequest importRequest) {
        commcareFormImporter.startImport(importRequest.getDateRange(), importRequest.getConfig());
    }
}
