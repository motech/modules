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

        String dateModifiedStart = "1900-01-01";
        String dateModifiedEnd = "2200-01-01";
        int recordCount = 0;
        int rowCount = 1;
        List<CaseInfo> caseRecordsList = null;
        CasesInfo casesInfo = null;

        if (settings.getSortDirection() != null) {
            sortAscending = "asc".equals(settings.getSortDirection());
        }

        if (settings.getPage() == null) {
            settings.setPage(1);
            settings.setRows(10);
        }

        if (settings.getDateModifiedStart() != null && !settings.getDateModifiedStart().isEmpty()) {
            dateModifiedStart = settings.getDateModifiedStart();
        }

        if (settings.getDateModifiedEnd() != null && !settings.getDateModifiedEnd().isEmpty()) {
            dateModifiedEnd = settings.getDateModifiedEnd();
        }

        settings.isFilter();

        switch (settings.getFilter()) {
            case "filerByName":
                casesInfo = caseService.getCasesByCasesNameWithMetadata(settings.getCaseName(), settings.getRows(), settings.getPage());
                caseRecordsList = casesInfo.getCaseInfoList();
                recordCount = casesInfo.getMetadataInfo().getTotalCount();
                break;
            case "filterByDateModified":
                casesInfo = caseService.getCasesByCasesTimeWithMetadata(dateModifiedStart, dateModifiedEnd, settings.getRows(), settings.getPage());
                caseRecordsList = casesInfo.getCaseInfoList();
                recordCount = casesInfo.getMetadataInfo().getTotalCount();
                break;
            case "filterByAll":
                casesInfo = caseService.getCasesByCasesNameAndTimeWithMetadata(settings.getCaseName(), dateModifiedStart, dateModifiedEnd, settings.getRows(), settings.getPage());
                caseRecordsList = casesInfo.getCaseInfoList();
                recordCount = casesInfo.getMetadataInfo().getTotalCount();
                break;
            default:
                casesInfo = caseService.getCasesWithMetadata(settings.getRows(), settings.getPage());
                caseRecordsList = casesInfo.getCaseInfoList();
                recordCount = casesInfo.getMetadataInfo().getTotalCount();
        }

        sortCases(sortAscending, caseRecordsList);
        rowCount = (int) Math.ceil(recordCount / (double) settings.getRows());

        return new CasesRecords(settings.getPage(), rowCount, recordCount, caseRecordsList);
    }

    private List<CaseInfo> sortCases(Boolean sortAscending, List<CaseInfo> caseRecordsList) {

        if (caseRecordsList != null && sortAscending) {
            Collections.sort(
                caseRecordsList, new Comparator<CaseInfo>() {
                    @Override
                    public int compare(CaseInfo o1, CaseInfo o2) {
                        return o1.getCaseName().compareToIgnoreCase(o2.getCaseName());
                    }
                }
            );
        } else if (caseRecordsList != null) {
            Collections.sort(
            caseRecordsList, Collections.reverseOrder(new Comparator<CaseInfo>() {
                @Override
                public int compare(CaseInfo o1, CaseInfo o2) {
                    return o1.getCaseName().compareToIgnoreCase(o2.getCaseName());
                }
            })
            );
        }

        return caseRecordsList;
    }
}
