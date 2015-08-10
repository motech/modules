package org.motechproject.commcare.util;

/**
 * Util class that allows to remove unnecessary characters from expression.
 */
public class NameTrimmer {

    /**
     * Removes all non-alphanumeric characters from the given expression.
     *
     * @param expression  the expression to be trimmed
     * @return the trimmed expression
     */
    public String trim(String expression) {
        return expression == null ? "" : expression.replaceAll("[^a-zA-Z0-9]", "");
    }
}
