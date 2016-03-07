package org.motechproject.odk.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import java.util.List;

/**
 * Represents a group value in a form instance
 */
@Entity
public class FormValueGroup extends FormValue {

    @Field
    private List<FormValue> children;


    public FormValueGroup(String name, String label, String type, List<FormValue> children) {
        super(name, label, type);
        this.children = children;
    }

    public List<FormValue> getChildren() {
        return children;
    }

    public void setChildren(List<FormValue> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "FormValueGroup{" +
                "children=" + children +
                '}';
    }
}
