package org.motechproject.ipf.domain;

import org.apache.commons.lang.ArrayUtils;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import java.util.Map;

@Entity
public class IPFTemplate {

    @Field(required = true)
    private String templateName;

    @Field(required = true)
    private Byte[] templateData;

    @Field(required = true)
    private Map<String, String> properties;

    public IPFTemplate() {
    }

    public IPFTemplate(String templateName, Byte[] templateData, Map<String, String> properties) {
        this.templateName = templateName;
        if (templateData != null) {
            this.templateData = templateData.clone();
        } else {
            this.templateData = ArrayUtils.EMPTY_BYTE_OBJECT_ARRAY;
        }
        this.properties = properties;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Byte[] getTemplateData() {
        return templateData.clone();
    }

    public void setTemplateData(Byte[] templateData) {
        this.templateData = templateData.clone();
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
}
