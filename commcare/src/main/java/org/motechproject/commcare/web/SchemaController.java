package org.motechproject.commcare.web;

import org.motechproject.commcare.domain.CaseInfo;
import org.motechproject.commcare.domain.CasesInfo;
import org.motechproject.commcare.domain.CommcareApplicationJson;
import org.motechproject.commcare.domain.report.ReportsMetadataInfo;
import org.motechproject.commcare.exception.ConfigurationNotFoundException;
import org.motechproject.commcare.service.CommcareApplicationDataService;
import org.motechproject.commcare.service.CommcareCaseService;
import org.motechproject.commcare.service.CommcareConfigService;
import org.motechproject.commcare.service.ReportsMetadataDataService;
import org.motechproject.commcare.web.domain.CasesRecords;
import org.motechproject.commcare.web.domain.GridSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.motechproject.commcare.util.Constants.HAS_MANAGE_COMMCARE_PERMISSION;

/**
 * The <code>SchemaController</code> is a spring controller class, providing a way to access data shown in the module
 * UI. It returns Commcare Application schema, containing forms and Cases. It is capable of handling multiple
 * configurations by parameterizing the endpoint URL.
 */
@Controller
@PreAuthorize(HAS_MANAGE_COMMCARE_PERMISSION)
public class SchemaController extends CommcareController {

    private static final int PAGE = 1;
    private static final int PAGE_SIZE = 10;

    @Autowired
    private CommcareApplicationDataService commcareApplicationDataService;

    @Autowired
    private ReportsMetadataDataService reportsMetadataDataService;

    @Autowired
    private CommcareCaseService caseService;

    @Autowired
    private CommcareConfigService configService;

    @RequestMapping(value = "/schema/{configName}")
    @ResponseBody
    public List<CommcareApplicationJson> schema(@PathVariable String configName) {
        validateConfig(configName);
        return commcareApplicationDataService.bySourceConfiguration(configName);
    }

    @RequestMapping(value = "/reports/{configName}")
    @ResponseBody
    public List<ReportsMetadataInfo> reports(@PathVariable String configName) {
        validateConfig(configName);
        return reportsMetadataDataService.bySourceConfiguration(configName);
    }

    @RequestMapping(value = "/caseList/{configName}")
    @ResponseBody
    public CasesRecords caseList(GridSettings settings, @PathVariable String configName) {
        Boolean sortAscending = true;

        String dateModifiedStart = "1900-01-01";
        String dateModifiedEnd = "2200-01-01";
        int recordCount;
        int rowCount;
        List<CaseInfo> caseRecordsList;
        CasesInfo casesInfo;

        if (settings.getSortDirection() != null) {
            sortAscending = "asc".equals(settings.getSortDirection());
        }

        if (settings.getPage() == null) {
            settings.setPage(PAGE);
            settings.setRows(PAGE_SIZE);
        }

        if (settings.getDateModifiedStart() != null && !settings.getDateModifiedStart().isEmpty()) {
            dateModifiedStart = settings.getDateModifiedStart();
        }

        if (settings.getDateModifiedEnd() != null && !settings.getDateModifiedEnd().isEmpty()) {
            dateModifiedEnd = settings.getDateModifiedEnd();
        }

        settings.determineFilter();

        switch (settings.getFilter()) {
            case "filerByName":
                casesInfo = caseService.getCasesByCasesNameWithMetadata(settings.getCaseName(), settings.getRows(),
                        settings.getPage(), configName);
                caseRecordsList = casesInfo.getCaseInfoList();
                recordCount = casesInfo.getMetadataInfo().getTotalCount();
                break;
            case "filterByDateModified":
                casesInfo = caseService.getCasesByCasesTimeWithMetadata(dateModifiedStart, dateModifiedEnd,
                        settings.getRows(), settings.getPage(), configName);
                caseRecordsList = casesInfo.getCaseInfoList();
                recordCount = casesInfo.getMetadataInfo().getTotalCount();
                break;
            case "filterByAll":
                casesInfo = caseService.getCasesByCasesNameAndTimeWithMetadata(settings.getCaseName(), dateModifiedStart,
                        dateModifiedEnd, settings.getRows(), settings.getPage(), configName);
                caseRecordsList = casesInfo.getCaseInfoList();
                recordCount = casesInfo.getMetadataInfo().getTotalCount();
                break;
            default:
                casesInfo = caseService.getCasesWithMetadata(settings.getRows(), settings.getPage(), configName);
                caseRecordsList = casesInfo.getCaseInfoList();
                recordCount = casesInfo.getMetadataInfo().getTotalCount();
        }

        sortCases(sortAscending, caseRecordsList);
        rowCount = (int) Math.ceil(recordCount / (double) settings.getRows());

        return new CasesRecords(settings.getPage(), rowCount, recordCount, caseRecordsList);
    }

    private void validateConfig(String configName) {

        if (!configService.exists(configName)) {
            throw new ConfigurationNotFoundException("Configuration with name\"" + configName + "\" doesn't exists!");
        }
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
