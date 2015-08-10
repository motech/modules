package org.motechproject.commcare.util;

import org.motechproject.commcare.domain.FormValueElement;

import java.util.HashMap;
import java.util.Map;

/**
 * Helper class that allows to parse XML representation of a form -
 * {@link org.motechproject.commcare.domain.FormValueElement}
 */
public final class FormValueParser {

    /**
     * Utility class, should not be initiated.
     */
    private FormValueParser() {
    }

    /**
     * Parses the <code>FormValueElement</code> into the map, where keys are form elements
     * and values represent the value of certain element.
     *
     * @param root Root of the XML representation of the form
     * @param prefix Prefix that should be injected before each key in the resulting map. Used to
     *                    represent nested elements in the XML.
     * @return a map of all elements in the given XML representation with values of these elements
     */
    public static Map<String, Object> parseFormToMap(FormValueElement root, String prefix) {
        Map<String, Object> result = new HashMap<>();
        if (root != null) {
            addParameters(root, result, prefix);
        }
        return result;
    }

    private static void addParameters(FormValueElement formValueElement, Map<String, Object> parameters, String paramPrefix) {
        for (Map.Entry<String, FormValueElement> entry : formValueElement.getSubElements().entries()) {
            // If there's a non-null value, add it to the parameters
            String value = entry.getValue().getValue();
            if (value != null) {
                parameters.put(paramPrefix + "/" + entry.getKey(), value);
            }

            // Call our method recursively for subelements
            addParameters(entry.getValue(), parameters, paramPrefix + "/" + entry.getKey());
        }
    }
}
