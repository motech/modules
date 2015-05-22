package org.motechproject.commcare.util;

/**
 * Util class that allows to remove unnecessary characters from expression
 */
public class NameTrimmer {

    public String trim(String expression) {
        // Remove all non-alphanumeric characters
        return expression == null ? "" : expression.replaceAll("[^a-zA-Z0-9]", "");
    }
}
