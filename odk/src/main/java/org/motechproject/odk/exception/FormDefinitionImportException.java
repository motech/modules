package org.motechproject.odk.exception;

/**
 * This exception is thrown either when an error is encountered while importing form definitions from one
 * of the external services.
 */
public class FormDefinitionImportException extends Exception {

    public FormDefinitionImportException() {
    }

    public FormDefinitionImportException(Throwable cause) {
        super(cause);
    }

    public FormDefinitionImportException(String message, Throwable cause) {
        super(message, cause);
    }
}
