package org.motechproject.mobileforms.validator;

import org.motechproject.mobileforms.domain.FormBean;
import org.motechproject.mobileforms.domain.FormBeanGroup;
import org.motechproject.mobileforms.domain.FormError;

import java.util.List;

public class TestFormValidator extends FormValidator<FormBean> {
    @Override
    public List<FormError> validate(FormBean formBean, FormBeanGroup formGroup, List<FormBean> allForms) {
        return super.validate(formBean, formGroup, allForms);
    }
}