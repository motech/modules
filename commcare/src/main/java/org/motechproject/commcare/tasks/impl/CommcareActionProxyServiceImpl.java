package org.motechproject.commcare.tasks.impl;

import org.joda.time.DateTime;
import org.motechproject.commcare.domain.CaseTask;
import org.motechproject.commcare.service.CaseActionService;
import org.motechproject.commcare.service.QueryStockLedgerActionService;
import org.motechproject.commcare.service.imports.ImportFormActionService;
import org.motechproject.commcare.tasks.CommcareActionProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("commcareActionProxyService")
public class CommcareActionProxyServiceImpl implements CommcareActionProxyService {

    private CaseActionService caseActionService;
    private ImportFormActionService importFormActionService;
    private QueryStockLedgerActionService queryStockLedgerActionService;

    @Autowired
    public  CommcareActionProxyServiceImpl(CaseActionService caseActionService,
                                           ImportFormActionService importFormActionService,
                                           QueryStockLedgerActionService queryStockLedgerActionService) {
        this.caseActionService = caseActionService;
        this.importFormActionService = importFormActionService;
        this.queryStockLedgerActionService = queryStockLedgerActionService;
    }


    @Override
    public CaseTask createCase(String configName, String caseType, String ownerId, String caseName, Map<String, Object> fieldValues) {
        return caseActionService.createCase(configName, caseType, ownerId, caseName, fieldValues);
    }

    @Override
    public void updateCase(String configName, String caseId, String ownerId, Boolean closeCase, Map<String, Object> fieldValues) {
        caseActionService.updateCase(configName, caseId, ownerId, closeCase, fieldValues);
    }

    @Override
    public void importForms(String configName, DateTime startDate, DateTime endDate) {
        importFormActionService.importForms(configName, startDate, endDate);
    }

    @Override
    public void queryStockLedger(String configName, String caseId, String sectionId, DateTime startDate, DateTime endDate, Map<String, Object> extraData) {
        queryStockLedgerActionService.queryStockLedger(configName, caseId, sectionId, startDate, endDate, extraData);
    }
}
