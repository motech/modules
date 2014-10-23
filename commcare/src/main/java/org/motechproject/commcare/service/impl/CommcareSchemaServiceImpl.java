package org.motechproject.commcare.service.impl;

import org.motechproject.commcare.domain.CommcareApplicationJson;
import org.motechproject.commcare.domain.CommcareModuleJson;
import org.motechproject.commcare.domain.FormSchemaJson;
import org.motechproject.commcare.service.CommcareApplicationDataService;
import org.motechproject.commcare.service.CommcareSchemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of the {@link org.motechproject.commcare.service.CommcareSchemaService}
 * Retrieves form and case schemas from MOTECH database.
 */
@Service
public class CommcareSchemaServiceImpl implements CommcareSchemaService {

    private CommcareApplicationDataService commcareApplicationDataService;

    @Override
    public List<FormSchemaJson> getAllFormSchemas() {
        List<FormSchemaJson> allFormSchemas = new ArrayList<>();

        for (CommcareApplicationJson app : commcareApplicationDataService.retrieveAll()) {
            for (CommcareModuleJson module : app.getModules()) {
                allFormSchemas.addAll(module.getFormSchemas());
            }
        }

        return allFormSchemas;
    }

    @Override
    public Map<String, Set<String>> getAllCaseTypes() {
        Map<String, Set<String>> allCaseTypes = new HashMap<>();

        for (CommcareApplicationJson app : commcareApplicationDataService.retrieveAll()) {
            for (CommcareModuleJson module : app.getModules()) {
                String caseType = module.getCaseType();
                if (!allCaseTypes.containsKey(caseType)) {
                    allCaseTypes.put(caseType, new HashSet<>(module.getCaseProperties()));
                }
            }
        }

        return allCaseTypes;
    }

    @Autowired
    public void setCommcareApplicationDataService(CommcareApplicationDataService commcareApplicationDataService) {
        this.commcareApplicationDataService = commcareApplicationDataService;
    }
}
