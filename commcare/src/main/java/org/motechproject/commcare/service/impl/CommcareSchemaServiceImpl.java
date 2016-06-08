package org.motechproject.commcare.service.impl;

import org.motechproject.commcare.domain.CommcareApplicationJson;
import org.motechproject.commcare.domain.CommcareModuleJson;
import org.motechproject.commcare.domain.FormSchemaJson;
import org.motechproject.commcare.service.CommcareApplicationDataService;
import org.motechproject.commcare.service.CommcareSchemaService;
import org.motechproject.commcare.tasks.builder.model.CaseTypeWithDisplayName;
import org.motechproject.commcare.tasks.builder.model.FormWithDisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

/**
 * Implementation of the {@link org.motechproject.commcare.service.CommcareSchemaService}
 * Retrieves form and case schemas from MOTECH database.
 */
@Service
public class CommcareSchemaServiceImpl implements CommcareSchemaService {

    private CommcareApplicationDataService commcareApplicationDataService;

    @Override
    @Transactional
    public List<FormSchemaJson> getAllFormSchemas(String configName) {
        List<FormSchemaJson> allFormSchemas = new ArrayList<>();

        for (CommcareApplicationJson app : commcareApplicationDataService.bySourceConfiguration(configName)) {
            for (CommcareModuleJson module : app.getModules()) {
                allFormSchemas.addAll(module.getFormSchemas());
            }
        }

        return allFormSchemas;
    }

    @Override
    @Transactional
    public Map<String, Set<String>> getAllCaseTypes(String configName) {
        Map<String, Set<String>> allCaseTypes = new HashMap<>();

        for (CommcareApplicationJson app : commcareApplicationDataService.bySourceConfiguration(configName)) {
            for (CommcareModuleJson module : app.getModules()) {
                String caseType = module.getCaseType();
                if (!allCaseTypes.containsKey(caseType)) {
                    allCaseTypes.put(caseType, new HashSet<>(module.getCaseProperties()));
                }
            }
        }

        return allCaseTypes;
    }

    @Override
    @Transactional
    public List<FormSchemaJson> getAllFormSchemas() {
        return getAllFormSchemas(null);
    }

    @Override
    @Transactional
    public Map<String, Set<String>> getAllCaseTypes() {
        return getAllCaseTypes(null);
    }
    
    @Override
    @Transactional
    public Set<CaseTypeWithDisplayName> getCaseTypesWithApplicationName(String configName) {
        Set<CaseTypeWithDisplayName> caseTypesWithApplicationName = new HashSet<>();

        for (CommcareApplicationJson app : commcareApplicationDataService.bySourceConfiguration(configName)) {
            for (CommcareModuleJson module : app.getModules()) {
                caseTypesWithApplicationName.add(new CaseTypeWithDisplayName(module.getCaseType(), app.getApplicationName()));
            }
        }

       return caseTypesWithApplicationName;
    }
    
    @Override
    @Transactional
    public Set<FormWithDisplayName> getFormsWithApplicationName(String configName) {
        Set<FormWithDisplayName> formWithDisplayName = new HashSet<>();

        for (CommcareApplicationJson app : commcareApplicationDataService.bySourceConfiguration(configName)) {
            for (CommcareModuleJson module : app.getModules()) {
                for (FormSchemaJson form : module.getFormSchemas()) {
                    formWithDisplayName.add(new FormWithDisplayName(form, app.getApplicationName()));
                }
            }
        }

        return formWithDisplayName;
    }

    @Override
    @Transactional
    public List<CommcareApplicationJson> retrieveApplications(String configName) {
        return commcareApplicationDataService.bySourceConfiguration(configName);
    }

    @Autowired
    public void setCommcareApplicationDataService(CommcareApplicationDataService commcareApplicationDataService) {
        this.commcareApplicationDataService = commcareApplicationDataService;
    }
}
