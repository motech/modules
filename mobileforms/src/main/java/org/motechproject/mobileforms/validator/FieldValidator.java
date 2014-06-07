package org.motechproject.mobileforms.validator;

import org.motechproject.mobileforms.domain.FormError;

import java.lang.annotation.Annotation;

public interface FieldValidator<T extends Annotation> {
    FormError validate(Object fieldValue, String fieldName, Class fieldType, T annotation);
}
