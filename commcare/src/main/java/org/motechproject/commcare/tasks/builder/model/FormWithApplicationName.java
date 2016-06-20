package org.motechproject.commcare.tasks.builder.model;

import org.motechproject.commcare.domain.FormSchemaJson;

import java.util.Objects;

/**
 * Stores form and it's application name.
 */

public class FormWithApplicationName {
    private FormSchemaJson form;
    private String applicationName;

    public FormWithApplicationName(FormSchemaJson form, String applicationName) {
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
        return Objects.equals(this, o);
    }

    @Override
    public int hashCode() {
        return 2 * form.hashCode() + 3 * applicationName.hashCode();
    }
}