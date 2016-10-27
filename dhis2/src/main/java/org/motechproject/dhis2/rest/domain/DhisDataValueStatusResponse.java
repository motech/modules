package org.motechproject.dhis2.rest.domain;


import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * A class to model DHIS2 responses that result from importing data values and data value sets.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DhisDataValueStatusResponse extends DhisResponse {

    private String description;
    private ImportCountDto dataValueCount;
    private boolean dataSetComplete;


    public ImportCountDto getDataValueCount() {
        return dataValueCount;
    }

    public void setDataValueCount(ImportCountDto dataValueCount) {
        this.dataValueCount = dataValueCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDataSetComplete() {
        return dataSetComplete;
    }

    public void setDataSetComplete(boolean dataSetComplete) {
        this.dataSetComplete = dataSetComplete;
    }
}
