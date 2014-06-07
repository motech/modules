package org.motechproject.sms.configs;

/**
 * Provider-specific properties. ie: Clickatell api_key, most will have at least username & password
 */
public class ConfigProp {
    private String name;
    private String value;

    public ConfigProp() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
