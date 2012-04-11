package org.motechproject.casexml.request;

public class Pregnancy {
    private String case_type;
    private String pregnancy_id;

    public Pregnancy(String pregnancyId, String caseType) {
        this.pregnancy_id = pregnancyId;
        this.case_type = caseType;
    }

    public String getCase_type() {
        return case_type;
    }

    public void setCase_type(String case_type) {
        this.case_type = case_type;
    }

    public String getPregnancy_id() {
        return pregnancy_id;
    }

    public void setPregnancy_id(String pregnancy_id) {
        this.pregnancy_id = pregnancy_id;
    }
}
