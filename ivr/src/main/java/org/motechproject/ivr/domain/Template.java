package org.motechproject.ivr.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.Unique;

/**
 * Represents a user-defined template, basically a string identified by a name which typically will be CCXML/VXML to be
 * returned to an IVR provider.
 * See https://velocity.apache.org/ for the template language rules.
 */
@Entity
public class Template {
    private static final int MAX_TEMPLATE_SIZE = 10240;

    @Field(required = true)
    @Unique
    private String name;

    @Field(required = true)
    @Column(length = MAX_TEMPLATE_SIZE)
    private String value;

    public Template(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof Template)) { return false; }

        Template template = (Template) o;

        if (name != null ? !name.equals(template.name) : template.name != null) { return false; }
        if (value != null ? !value.equals(template.value) : template.value != null) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Template{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
