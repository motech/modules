package org.motechproject.dhis2.rest.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Models the DHIS2 API's Data Set resource.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataSetDto extends BaseDto {

    private List<DataElementDto> dataElements;

    public List<DataElementDto> getDataElements() {
        return dataElements;
    }

    public void setDataElements(List<DataElementDto> dataElements) {
        this.dataElements = dataElements;
    }
}
