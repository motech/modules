package org.motechproject.dhis2.rest.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A class to model DHIS2 responses that result from importing data values and data value sets.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DhisDataValueStatusResponse {

    private DhisStatus status;
    private String description;
    private ImportCountDto importCountDto;
    private ImportCountDto dataValueCount;
    private boolean dataSetComplete;


    public ImportCountDto getDataValueCount() {
        return dataValueCount;
    }

    public void setDataValueCount(ImportCountDto dataValueCount) {
        this.dataValueCount = dataValueCount;
    }

    public DhisStatus getStatus() {
        return status;
    }

    public void setStatus(DhisStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ImportCountDto getImportCountDto() {
        return importCountDto;
    }

    public void setImportCountDto(ImportCountDto importCountDto) {
        this.importCountDto = importCountDto;
    }

    public boolean isDataSetComplete() {
        return dataSetComplete;
    }

    public void setDataSetComplete(boolean dataSetComplete) {
        this.dataSetComplete = dataSetComplete;
    }
}
