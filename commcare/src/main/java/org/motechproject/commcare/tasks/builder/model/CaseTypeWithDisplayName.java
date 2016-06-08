package org.motechproject.commcare.tasks.builder.model;

public class CaseTypeWithDisplayName {
    private String caseType;

    private String displayName;

    public CaseTypeWithDisplayName(String caseType, String displayName) {
        this.caseType = caseType;
        this.displayName = displayName;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public boolean equals(Object obj) {
        if (caseType.equals((String) obj)) {
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return caseType.hashCode();
    }
}
