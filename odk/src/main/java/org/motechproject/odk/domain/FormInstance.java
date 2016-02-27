package org.motechproject.odk.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.jdo.annotations.Unique;
import java.util.List;

/**
 * Represents an instance of a form.
 */
@Entity
public class FormInstance {

    @Field
    private String title;

    @Field
    private String configName;

    @Field
    @Unique
    private String instanceId;

    @Field(name = "form_values")
    private List<FormValue> formValues;

    public FormInstance(String title, String configName, String instanceId) {
        this.title = title;
        this.configName = configName;
        this.instanceId = instanceId;
    }

    public FormInstance() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<FormValue> getFormValues() {
        return formValues;
    }

    public void setFormValues(List<FormValue> formValues) {
        this.formValues = formValues;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }


    @Override
    public String toString() {
        return "FormInstance{" +
                "title='" + title + '\'' +
                ", configName='" + configName + '\'' +
                ", instanceId='" + instanceId + '\'' +
                ", formValues=" + formValues +
                '}';
    }
}
