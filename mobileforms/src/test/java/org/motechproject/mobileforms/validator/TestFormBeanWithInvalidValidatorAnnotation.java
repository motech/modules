package org.motechproject.mobileforms.validator;

import org.motechproject.mobileforms.domain.FormBean;

public class TestFormBeanWithInvalidValidatorAnnotation extends FormBean {
    @InvalidValidationMarker
    private String familyName;

    @Override
    public String groupId() {
        return familyName;
    }

    public TestFormBeanWithInvalidValidatorAnnotation(String familyName) {
        this.familyName = familyName;
    }

    public String getFamilyName() {
        return familyName;
    }
}
