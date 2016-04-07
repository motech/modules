package org.motechproject.odk.domain;


import org.joda.time.DateTime;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

@Entity
public class FormValueDateTime extends FormValue {

    @Field
    private DateTime value;

    public FormValueDateTime(String name, String label, String type, DateTime value) {
        super(name, label, type);
        this.value = value;
    }

    public DateTime getValue() {
        return value;
    }

    public void setValue(DateTime value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "FormValueDateTime{" +
                "value=" + value +
                '}';
    }
}
