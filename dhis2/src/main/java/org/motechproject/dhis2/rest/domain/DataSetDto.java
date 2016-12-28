package org.motechproject.dhis2.rest.domain;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.ArrayList;
import java.util.List;

/**
 * Models the DHIS2 API's Data Set resource.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataSetDto extends BaseDto {

    private List<DataElementDto> dataElements;

    public List<DataElementDto> getDataElements() {
        if (dataElements == null) {
            dataElements = new ArrayList<>();
        }

        return dataElements;
    }

    public void setDataElements(List<DataElementDto> dataElements) {
        this.dataElements = dataElements;
    }
}
