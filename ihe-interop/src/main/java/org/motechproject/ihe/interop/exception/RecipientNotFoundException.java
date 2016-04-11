package org.motechproject.ihe.interop.exception;

/**
 * The <code>RecipientNotFoundException</code> exception signals a situation in which a recipient with
 * a given name does not exist.
 */
public class RecipientNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 5352533091464541891L;

    public RecipientNotFoundException(String templateName) {
        super(String.format("Cannot find recipient with %s name", templateName));
    }
}
