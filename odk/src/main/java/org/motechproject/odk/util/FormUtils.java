package org.motechproject.odk.util;

import org.motechproject.odk.exception.MalformedUriException;

/**
 * Utility class for form parsers.
 */
public final class FormUtils {

    private FormUtils() {

    }

    /**
     * Attempts to remove the form name from the Form Element label. If an {@link IndexOutOfBoundsException} occurs,
     * it will return the label unmodified.
     * @param label the label of a Form Element
     * @return The modified label.
     */
    public static String removeFormNameFromLabel(String label) {
        try {
            return label.substring(label.substring(1).indexOf('/') + 1);
        } catch (IndexOutOfBoundsException e) {
            return label;
        }
    }

    /**
     * Attempts to create a child Form element label from its parent label. If an {@link IndexOutOfBoundsException} occurs,
     * it will return the child's name.
     * @param parentLabel The label of the parent form element
     * @param childName The name of the child form element
     * @return The modified child label.
     */
    public static String createChildLabelFromParentLabel(String parentLabel, String childName) {
        try {
            return parentLabel + "/" + childName.substring(childName.lastIndexOf('/') + 1);
        } catch (IndexOutOfBoundsException e) {
            return childName;
        }
    }

    /**
     * Removes the title from the URI fully qualified URI.
     * @param uri The name of the form field
     * @return A new URI string with the title removed
     * @throws MalformedUriException if the URI is malformed
     */
    public static String removeTitleFromUri(String uri) throws MalformedUriException {
        try {
            return uri.substring(uri.indexOf('/', 1) + 1, uri.length());
        } catch (IndexOutOfBoundsException e) {
            throw new MalformedUriException("Error removing form title from form field name: " + uri, e);
        }
    }

    /**
     * Returns just the form field name from the fully qualified URI.
     * @param uri The
     * @return A new string containing the form field name.
     * @throws MalformedUriException if the URI is malformed
     */
    public static String getFieldNameFromURI(String uri) throws MalformedUriException{
        try {
            String[] split = uri.split("/");
            return split[split.length - 1];
        } catch (IndexOutOfBoundsException e) {
            throw new MalformedUriException("Error getting field name from URI: " + uri, e);
        }
    }

}
