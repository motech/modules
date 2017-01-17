package org.motechproject.commcare.service.impl;

import org.motechproject.commcare.domain.CommcareApplicationJson;
import org.motechproject.commcare.domain.CommcareModuleJson;
import org.motechproject.commcare.domain.FormSchemaJson;
import org.motechproject.commcare.domain.report.ReportsMetadataInfo;
import org.motechproject.commcare.service.CommcareApplicationDataService;
import org.motechproject.commcare.service.CommcareSchemaService;
import org.motechproject.commcare.service.ReportsMetadataDataService;
import org.motechproject.commcare.tasks.builder.model.CaseTypeWithApplicationName;
import org.motechproject.commcare.tasks.builder.model.FormWithApplicationName;
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

    private ReportsMetadataDataService reportsMetadataDataService;

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
    public Set<CaseTypeWithApplicationName> getCaseTypesWithApplicationName(String configName) {
        Set<CaseTypeWithApplicationName> caseTypesWithApplicationName = new HashSet<>();

        for (CommcareApplicationJson app : commcareApplicationDataService.bySourceConfiguration(configName)) {
            for (CommcareModuleJson module : app.getModules()) {
                caseTypesWithApplicationName.add(new CaseTypeWithApplicationName(module.getCaseType(), app.getApplicationName(), module.getCaseProperties()));
            }
        }

       return caseTypesWithApplicationName;
    }
    
    @Override
    @Transactional
    public Set<FormWithApplicationName> getFormsWithApplicationName(String configName) {
        Set<FormWithApplicationName> formWithApplicationName = new HashSet<>();

        for (CommcareApplicationJson app : commcareApplicationDataService.bySourceConfiguration(configName)) {
            for (CommcareModuleJson module : app.getModules()) {
                for (FormSchemaJson form : module.getFormSchemas()) {
                    formWithApplicationName.add(new FormWithApplicationName(form, app.getApplicationName()));
                }
            }
        }

        return formWithApplicationName;
    }

    @Override
    @Transactional
    public List<CommcareApplicationJson> retrieveApplications(String configName) {
        return commcareApplicationDataService.bySourceConfiguration(configName);
    }

    @Override
    @Transactional
    public List<ReportsMetadataInfo> getReportsMetadata (String configName) {
        return reportsMetadataDataService.bySourceConfiguration(configName);
    }

    @Autowired
    public void setCommcareApplicationDataService(CommcareApplicationDataService commcareApplicationDataService) {
        this.commcareApplicationDataService = commcareApplicationDataService;
    }

    @Autowired
    public void setReportsMetadataDataService (ReportsMetadataDataService reportsMetadataDataService) {
        this.reportsMetadataDataService = reportsMetadataDataService;
    }
}
