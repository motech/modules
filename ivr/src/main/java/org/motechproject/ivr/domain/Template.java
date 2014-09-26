package org.motechproject.ivr.domain;

/**
 * Represents a user-defined template, basically a string identified by a name which typically will be CCXML/VXML to be
 * returned to an IVR provider.
 * See https://velocity.apache.org/ for the template language rules.
 */
public class Template {
    /**
     * Template name, how it's identified by the IVR provider. The {template} part in:
     * http://server/motech-platform-server/module/ivr/template/{config}/{template}
     */
    private String name;
    /**
     * The template contents: what's returned to the IVR provider when it sends a /template HTTP request
     *
     * Note: this is a velocity template which is merged with the values provided in the query param
     *       see https://velocity.apache.org/engine/
     */
    private String value;

    public Template(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
