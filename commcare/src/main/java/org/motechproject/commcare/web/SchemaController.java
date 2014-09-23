package org.motechproject.commcare.web;

import org.motechproject.commcare.domain.CaseInfo;
import org.motechproject.commcare.domain.CasesInfo;
import org.motechproject.commcare.domain.CommcareApplicationJson;
import org.motechproject.commcare.service.CommcareApplicationDataService;
import org.motechproject.commcare.service.CommcareCaseService;
import org.motechproject.commcare.web.domain.CasesRecords;
import org.motechproject.commcare.web.domain.GridSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * The <code>SchemaController</code> is a spring controller class, providing
 * a way to access data shown in the module UI. It returns Commcare Application schema,
 * containing forms and Cases.
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
    public CasesRecords caseList(GridSettings settings) {
        Boolean sortAscending = true;

        if (settings.getSortDirection() != null) {
            sortAscending = "asc".equals(settings.getSortDirection());
        }

        if (settings.getPage() == null) {
            settings.setPage(1);
            settings.setRows(10);
        }

        CasesInfo casesInfo = caseService.getCasesWithMetadata(settings.getRows(), settings.getPage());
        List<CaseInfo> caseRecordsList = casesInfo.getCaseInfoList();

        if (sortAscending) {
            Collections.sort(
                    caseRecordsList, new Comparator<CaseInfo>() {
                    @Override
                    public int compare(CaseInfo o1, CaseInfo o2) {
                        return o1.getCaseName().compareToIgnoreCase(o2.getCaseName());
                    }
                }
            );
        } else {
            Collections.sort(
                caseRecordsList, Collections.reverseOrder(new Comparator<CaseInfo>() {
                @Override
                public int compare(CaseInfo o1, CaseInfo o2) {
                    return o1.getCaseName().compareToIgnoreCase(o2.getCaseName());
                }
            })
            );
        }

        int recordCount = casesInfo.getMetadataInfo().getTotalCount();
        int rowCount = (int) Math.ceil(recordCount / (double) settings.getRows());

        return new CasesRecords(settings.getPage(), rowCount, recordCount, caseRecordsList);
    }
}
