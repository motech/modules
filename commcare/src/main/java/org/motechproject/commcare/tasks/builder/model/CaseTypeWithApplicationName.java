package org.motechproject.commcare.tasks.builder.model;

/**
* Stores casetype and it's application name.
 */

public class CaseTypeWithApplicationName {
    private String caseType;
    private String applicationName;

    public CaseTypeWithApplicationName(String caseType, String applicationName) {
        this.caseType = caseType;
        this.applicationName = applicationName;
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

    @Override
    public boolean equals(Object obj) {
        if (caseType.equals(obj)) {
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return caseType.hashCode();
    }
}