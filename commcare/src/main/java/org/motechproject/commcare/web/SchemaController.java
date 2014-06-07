package org.motechproject.commcare.web;

import org.motechproject.commcare.domain.CaseInfo;
import org.motechproject.commcare.domain.CommcareApplicationJson;
import org.motechproject.commcare.service.CommcareApplicationDataService;
import org.motechproject.commcare.service.CommcareCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Controller that handles the incoming full form feed from CommCareHQ.
 */
@Controller
public class SchemaController {

    @Autowired
    private CommcareApplicationDataService commcareApplicationDataService;

    @Autowired
    private CommcareCaseService caseService;

    @RequestMapping(value = "/schema")
    @ResponseBody
    public List<CommcareApplicationJson> schema() {
        return commcareApplicationDataService.retrieveAll();
    }

    @RequestMapping(value = "/caseList")
    @ResponseBody
    public List<CaseInfo> caseList() {
        return caseService.getAllCases();
    }

}
