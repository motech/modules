package org.motechproject.dhis2.rest.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A class to model DHIS2 response details that result from creating entities.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DhisResponseDetails {

    private String responseType;
    private DhisStatus status;
    private String reference;
    private ImportCountDto importCount;

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public DhisStatus getStatus() {
        return status;
    }

    public void setStatus(DhisStatus status) {
        this.status = status;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public ImportCountDto getImportCount() {
        return importCount;
    }

    public void setImportCount(ImportCountDto importCount) {
        this.importCount = importCount;
    }
}
