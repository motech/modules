package org.motechproject.commcare.domain;

/**
 * Represents a form value attribute.
 */
public class FormValueAttribute implements FormNode {

    private final String value;

    /**
     * Creates new form value attribute with value set to {@code value}.
     *
     * @param value  the value of the attribute
     */
    public FormValueAttribute(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
         return value;
    }
}
