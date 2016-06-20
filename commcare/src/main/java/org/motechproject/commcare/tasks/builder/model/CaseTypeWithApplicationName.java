package org.motechproject.commcare.tasks.builder.model;

import java.util.List;
import java.util.Objects;

/**
* Stores casetype and it's application name.
 */

public class CaseTypeWithApplicationName {
    private String caseType;
    private String applicationName;
    private List<String> caseProperties;

    public CaseTypeWithApplicationName(String caseType, String applicationName, List<String> caseProperties) {
        this.caseType = caseType;
        this.applicationName = applicationName;
        this.caseProperties = caseProperties;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public List<String> getCaseProperties() {
        return caseProperties;
    }

    public void setCaseProperties(List<String> caseProperties) {
        this.caseProperties = caseProperties;
    }

    @Override
    public boolean equals(Object obj) {
        return Objects.equals(this, obj);
    }

    @Override
    public int hashCode() {
        return 2*caseType.hashCode() + 3*applicationName.hashCode() + 10*caseProperties.hashCode();
    }
}