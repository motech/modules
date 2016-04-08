package org.motechproject.ihe.interop.domain;

import org.apache.commons.lang.ArrayUtils;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import java.util.Map;

/**
 * Represents CDA template data.
 */
@Entity
public class CdaTemplate {

    @Field
    private String templateName;

    @Field
    private Byte[] templateData;

    @Field
    private Map<String, String> properties;

    public CdaTemplate() {
    }

    public CdaTemplate(String templateName, Byte[] templateData, Map<String, String> properties) {
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
