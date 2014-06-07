package org.motechproject.mobileforms.validator.annotations;

import org.motechproject.mobileforms.validator.FieldValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidationMarker {
    Class<? extends FieldValidator> handler();
}
