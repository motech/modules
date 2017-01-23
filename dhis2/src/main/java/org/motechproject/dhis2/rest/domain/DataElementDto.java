package org.motechproject.dhis2.rest.domain;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * A class to model the DHIS2 API's data element resource.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataElementDto extends BaseDto {

    public DataElementDto() {}

    public DataElementDto(String uuid, String name) {
        this.setId(uuid);
        this.setName(name);
    }
}
