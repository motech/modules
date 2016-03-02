package org.motechproject.odk.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

/**
 * Domain object relating to a form receipt failure event
 */
@Entity
public class FormFailure {

    @Field
    private String configName;

    @Field
    private String formTitle;

    @Field
    private String message;

    @Field
    private String exception;

    @Field(type = "TEXT")
    private String jsonContent;

    public FormFailure(String configName, String formTitle, String message, String exception, String jsonContent) {
        this.configName = configName;
        this.formTitle = formTitle;
        this.message = message;
        this.exception = exception;
        this.jsonContent = jsonContent;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getFormTitle() {
        return formTitle;
    }

    public void setFormTitle(String formTitle) {
        this.formTitle = formTitle;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getJsonContent() {
        return jsonContent;
    }

    public void setJsonContent(String jsonContent) {
        this.jsonContent = jsonContent;
    }
}
