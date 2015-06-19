package org.motechproject.openmrs19.resource.model;

import org.motechproject.openmrs19.resource.model.Attribute.AttributeType;

import java.util.List;

/**
 * Stores a list of {@link AttributeType}s. This class is used as a result of a query to the OpenMRS server.
 */
public class AttributeTypeListResult {

    private List<AttributeType> results;

    public List<AttributeType> getResults() {
        return results;
    }

    public void setResults(List<AttributeType> results) {
        this.results = results;
    }
}
