package org.motechproject.ivr.exception;

/**
 * The <code>TemplateNotFoundException</code> exception signals a situation when a Velocity template
 * with a given name cannot be found.
 */
public class TemplateNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 3498146050497439645L;

    public TemplateNotFoundException(String message) {
        super(message);
    }

    public TemplateNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
