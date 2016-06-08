package org.motechproject.commcare.tasks.builder.model;

import org.motechproject.commcare.domain.FormSchemaJson;

public class FormWithDisplayName {
    private FormSchemaJson form;

    private String displayName;

    public FormWithDisplayName(FormSchemaJson form, String displayName) {
        this.form = form;
        this.displayName = displayName;
    }

    public FormSchemaJson getForm() {
        return form;

    }

    public void setForm(FormSchemaJson form) {
        this.form = form;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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
