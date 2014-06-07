package org.motechproject.commcare.domain;

import com.google.gson.annotations.SerializedName;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

@Entity(name = "Form Schema Question Option")
public class FormSchemaQuestionOptionJson {

    @Field(displayName = "Label")
    @SerializedName("label")
    private String label;

    @Field(displayName = "Value")
    @SerializedName("value")
    private String value;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
