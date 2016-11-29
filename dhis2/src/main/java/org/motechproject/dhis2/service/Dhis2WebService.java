package org.motechproject.dhis2.service;

import org.motechproject.dhis2.dto.DataElementDto;
import org.motechproject.dhis2.dto.OrgUnitDto;
import org.motechproject.dhis2.dto.ProgramDto;
import org.motechproject.dhis2.dto.TrackedEntityAttributeDto;
import org.motechproject.dhis2.dto.TrackedEntityDto;

import java.util.List;

/**
 * This interface is used by Dhis2 controllers to work with dto objects.
 */
public interface Dhis2WebService {

    /**
     * @see DataElementService#findAll()
     */
    List<DataElementDto> getDataElements();

    /**
     * @see OrgUnitService#findAll()
     */
    List<OrgUnitDto> getOrgUnits();

    /**
     * @see TrackedEntityService#findAll()
     */
    List<TrackedEntityDto> getTrackedEntities();

    /**
     * @see TrackedEntityAttributeService#findAll()
     */
    List<TrackedEntityAttributeDto> getAttributes();

    /**
     * @see ProgramService#findAll()
     */
    List<ProgramDto> getPrograms();
}

