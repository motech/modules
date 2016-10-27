package org.motechproject.dhis2.rest.domain;


import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.Objects;

/**
 * A class to model DHIS2 responses that result from creating entities.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DhisStatusResponse extends DhisResponse{

    private DhisStatus status;
    private ImportCountDto importCountDto;
    private String reference;
    private DhisResponseDetails response;

    public ImportCountDto getImportCountDto() {
        if (importCountDto == null && response == null) {
            return null;
        } else {
            return importCountDto != null ? importCountDto : response.getImportCount();
        }
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
        return Objects.hash(status, importCountDto, reference, response);
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
                && Objects.equals(this.importCountDto, other.importCountDto)
                && Objects.equals(this.reference, other.reference)
                && Objects.equals(this.response, other.response);
    }


}
