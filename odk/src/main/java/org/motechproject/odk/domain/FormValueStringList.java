package org.motechproject.odk.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import java.util.List;


/**
 * Represents a collection of String values in a form instance
 */
@Entity
public class FormValueStringList extends FormValue {

    @Field
    private List<String> value;

    public FormValueStringList(String name, String label, String type, List<String> value) {
        super(name, label, type);
        this.value = value;
    }

    public List<String> getValue() {
        return value;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "FormValueStringList{" +
                "value=" + value +
                '}';
    }
}
