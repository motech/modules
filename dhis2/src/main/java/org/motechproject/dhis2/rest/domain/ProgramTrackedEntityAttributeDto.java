package org.motechproject.dhis2.rest.domain;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * A class to model program tracked entity attributes returned by the DHIS2 API.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProgramTrackedEntityAttributeDto extends BaseDto{

    private TrackedEntityAttributeDto trackedEntityAttribute;

    public TrackedEntityAttributeDto getTrackedEntityAttribute() {
        return trackedEntityAttribute;
    }

    public void setTrackedEntityAttribute(TrackedEntityAttributeDto trackedEntityAttribute) {
        this.trackedEntityAttribute = trackedEntityAttribute;
    }
}
