package org.motechproject.dhis2.rest.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

/**
 * A class to model DHIS2 responses that result from creating entities.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DhisStatusResponse {

    private DhisStatus status;
    private ImportCountDto importCount;
    private String reference;
    private DhisResponseDetails response;

    public DhisStatus getStatus() {
        return status;
    }

    public void setStatus(DhisStatus status) {
        this.status = status;
    }

    public ImportCountDto getImportCount() {
        if (importCount == null && response == null) {
            return null;
        } else {
            return importCount != null ? importCount : response.getImportCount();
        }
    }

    public void setImportCount(ImportCountDto importCount) {
        this.importCount = importCount;
    }

    public String getReference() {
        if (reference == null && response == null) {
            return null;
        } else {
            return reference != null ? reference : response.getReference();
        }
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public DhisResponseDetails getResponse() {
        return response;
    }

    public void setResponse(DhisResponseDetails response) {
        this.response = response;
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, importCount, reference, response);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final DhisStatusResponse other = (DhisStatusResponse) obj;
        return Objects.equals(this.status, other.status)
                && Objects.equals(this.importCount, other.importCount)
                && Objects.equals(this.reference, other.reference)
                && Objects.equals(this.response, other.response);
    }


}
