package org.motechproject.odk.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

/**
 * Represents a double value in a form instance
 */
@Entity
public class FormValueDouble extends FormValue {

    @Field
    private Double value;

    public FormValueDouble(String name, String label, String type, Double value) {
        super(name, label, type);
        this.value = value;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "FormValueDouble{" +
                "value=" + value +
                '}';
    }
}
