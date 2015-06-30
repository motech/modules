package org.motechproject.sms.configs;

/**
 * Provider-specific properties. ie: Clickatell api_key, most will have at least username & password
 */
public class ConfigProp {

    /**
     * The name of the property.
     */
    private String name;

    /**
     * The value of the property.
     */
    private String value;

    /**
     * Constructs a new prop without a name and a value.
     */
    public ConfigProp() {
    }

    /**
     * Constructs the property using the given name and value
     * @param name the name of the property
     * @param value the value of the property
     */
    public ConfigProp(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * @return the name of the property
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name of the property
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the value of the property
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value of the property
     */
    public void setValue(String value) {
        this.value = value;
    }
}
