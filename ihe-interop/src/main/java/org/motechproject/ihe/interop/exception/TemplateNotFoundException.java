package org.motechproject.ihe.interop.exception;

/**
 * The <code>TemplateNotFoundException</code> exception signals a situation in which a template with
 * a given name does not exist in database.
 */
public class TemplateNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 9078408685461936215L;

    public TemplateNotFoundException(String templateName) {
        super(String.format("Cannot find template with %s name", templateName));
    }
}
