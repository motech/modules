package org.motechproject.mobileforms.validator.impl;

import org.apache.commons.lang.StringUtils;
import org.motechproject.mobileforms.domain.FormError;
import org.motechproject.mobileforms.validator.FieldValidator;
import org.motechproject.mobileforms.validator.annotations.Required;

public class RequiredValidator implements FieldValidator<Required> {

    @Override
    public FormError validate(Object fieldValue, String fieldName, Class fieldType, Required annotation) {
        if (fieldValue == null || fieldType == String.class && StringUtils.isEmpty((String) fieldValue)) {
            return new FormError(fieldName, "is mandatory");
        }
        return null;
    }
}
