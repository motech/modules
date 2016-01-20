package org.motechproject.odk.domain;

import java.util.Arrays;
import java.util.Map;

/**
 * Container for the form data in an ODK form publication.
 */
public class OdkJsonFormPublication {

    private String token;
    private String content;
    private String formId;
    private String formVersion;

    private Map<String, Object>[] data;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public Map<String, Object>[] getData() {
        return Arrays.copyOf(data, data.length);
    }

    public void setData(Map<String, Object>[] data) {
        this.data = Arrays.copyOf(data, data.length);
    }

    public String getFormVersion() {
        return formVersion;
    }

    public void setFormVersion(String formVersion) {
        this.formVersion = formVersion;
    }
}
