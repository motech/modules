package org.motechproject.commcare.tasks.builder.model;

import org.motechproject.commcare.domain.FormSchemaJson;

/**
 * Stores form and it's application name.
 */

public class FormWithDisplayName {
    private FormSchemaJson form;
    private String applicationName;

    public FormWithDisplayName(FormSchemaJson form, String applicationName) {
        this.form = form;
        this.applicationName = applicationName;
    }

    public FormSchemaJson getForm() {
        return form;
    }

    public void setForm(FormSchemaJson form) {
        this.form = form;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    @Override
    public boolean equals(Object o) {
        FormWithDisplayName that = (FormWithDisplayName) o;

        return form.equals(that.form);
    }

    @Override
    public int hashCode() {
        return form.hashCode();
    }
}