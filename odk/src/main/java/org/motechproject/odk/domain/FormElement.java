package org.motechproject.odk.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.odk.constant.FieldTypeConstants;

import java.util.List;

/**
 * Represents an element in a form definition.
 */

@Entity
public class FormElement {

    @Field
    private String name;

    @Field
    private String label;

    @Field
    private String type;

    @Field
    private List<FormElement> children;

    @Field
    private boolean partOfRepeatGroup;


    public FormElement(String name, String label, String type, List<FormElement> children, boolean partOfRepeatGroup) {
        this.name = name;
        this.label = label;
        this.type = type;
        this.children = children;
        this.partOfRepeatGroup = partOfRepeatGroup;
    }

    public FormElement() {
    }

    public boolean hasChildren() {
        return children != null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<FormElement> getChildren() {
        return children;
    }

    public void setChildren(List<FormElement> children) {
        this.children = children;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isPartOfRepeatGroup() {
        return partOfRepeatGroup;
    }

    public boolean isRepeatGroup() {
        return getType().equals(FieldTypeConstants.REPEAT_GROUP);
    }

    public void setPartOfRepeatGroup(boolean partOfRepeatGroup) {
        this.partOfRepeatGroup = partOfRepeatGroup;
    }

    @Override
    public String toString() {
        return "FormElement{" +
                "name='" + name + '\'' +
                ", label='" + label + '\'' +
                ", type='" + type + '\'' +
                ", children=" + children +
                ", partOfRepeatGroup=" + partOfRepeatGroup +
                '}';
    }
}
