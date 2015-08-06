package org.motechproject.commcare.domain;

import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Represents a value for a metadata entry in a Commcare form. Can have multiple string values.
 */
public class MetadataValue implements Serializable {

    private static final long serialVersionUID = 5276103973866677518L;

    /**
     * The values for this metadata, stored as a collection, since there can be multiple.
     */
    private Collection<String> values;

    /**
     * Creates this object from a single value.
     * @param value the metadata value
     */
    public MetadataValue(String value) {
        values = new ArrayList<>(Collections.singletonList(value));
    }

    /**
     * Creates this object from a collection of values.
     * @param values the metadata values
     */
    public MetadataValue(Collection<String> values) {
        this.values = values;
    }

    /**
     * @return the values collection
     */
    public Collection<String> getValues() {
        return values;
    }

    /**
     * @param values the values collection
     */
    public void setValues(Collection<String> values) {
        this.values = values;
    }

    /**
     * Returns the first value from the metadata. Can be used to easily retrieve
     * the value if we know that this entry has only a single value.
     * @return the first value, or null if there are no values
     */
    public String firstValue() {
        return CollectionUtils.isEmpty(values) ? null : values.iterator().next();
    }
}
