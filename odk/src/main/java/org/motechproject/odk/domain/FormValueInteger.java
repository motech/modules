package org.motechproject.odk.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

/**
 * Represents an integer value in a form instance
 */
@Entity
public class FormValueInteger extends FormValue {

    @Field
    private Integer value;

    public FormValueInteger(String name, String label, String type, Integer value) {
        super(name, label, type);
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "FormValueInteger{" +
                "value=" + value +
                '}';
    }
}
