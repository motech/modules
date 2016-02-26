package org.motechproject.odk.exception;

import org.motechproject.odk.domain.builder.FormInstanceBuilder;

/**
 * This exception is thrown by an instance of the {@link FormInstanceBuilder} class  when an error is encountered
 * while building a {@link org.motechproject.odk.domain.FormInstance}.
 */
public class FormInstanceBuilderException extends Exception {

    public FormInstanceBuilderException(String message, Throwable cause) {
        super(message, cause);
    }
}
