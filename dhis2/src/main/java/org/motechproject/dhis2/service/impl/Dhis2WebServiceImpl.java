package org.motechproject.dhis2.service.impl;

import org.motechproject.dhis2.domain.DataElement;
import org.motechproject.dhis2.domain.OrgUnit;
import org.motechproject.dhis2.domain.Program;
import org.motechproject.dhis2.domain.TrackedEntity;
import org.motechproject.dhis2.domain.TrackedEntityAttribute;
import org.motechproject.dhis2.dto.DataElementDto;
import org.motechproject.dhis2.dto.OrgUnitDto;
import org.motechproject.dhis2.dto.ProgramDto;
import org.motechproject.dhis2.dto.TrackedEntityAttributeDto;
import org.motechproject.dhis2.dto.TrackedEntityDto;
import org.motechproject.dhis2.service.DataElementService;
import org.motechproject.dhis2.service.Dhis2WebService;
import org.motechproject.dhis2.service.OrgUnitService;
import org.motechproject.dhis2.service.ProgramService;
import org.motechproject.dhis2.service.TrackedEntityAttributeService;
import org.motechproject.dhis2.service.TrackedEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * The implementation of the {@link Dhis2WebService} interface.
 */
@Service("dhis2WebService")
public class Dhis2WebServiceImpl implements Dhis2WebService {

    @Autowired
    private DataElementService dataElementService;

    @Autowired
    private OrgUnitService orgUnitService;

    @Autowired
    private TrackedEntityAttributeService trackedEntityAttributeService;

    @Autowired
    private TrackedEntityService trackedEntityService;

    @Autowired
    private ProgramService programService;

    @Override
    @Transactional
    public List<DataElementDto> getDataElements() {
        return dataElementsToDtos(dataElementService.findAll());
    }

    @Override
    @Transactional
    public List<OrgUnitDto> getOrgUnits() {
        return orgUnitsToDtos(orgUnitService.findAll());
    }

    @Override
    @Transactional
    public List<TrackedEntityDto> getTrackedEntities() {
        return trackedEntitiesToDtos(trackedEntityService.findAll());
    }

    @Override
    @Transactional
    public List<TrackedEntityAttributeDto> getAttributes() {
        return attributesToDtos(trackedEntityAttributeService.findAll());
    }

    @Override
    @Transactional
    public List<ProgramDto> getPrograms() {
        return programsToDtos(programService.findAll());
    }

    private List<DataElementDto> dataElementsToDtos(List<DataElement> dataElementsList) {
        List<DataElementDto> dataElementDtos = new ArrayList<>();

        for (DataElement dataElement : dataElementsList) {
            dataElementDtos.add(dataElement.toDto());
        }

        return dataElementDtos;
    }

    private List<OrgUnitDto> orgUnitsToDtos(List<OrgUnit> orgUnitList) {
        List<OrgUnitDto> orgUnitDtos = new ArrayList<>();

        for (OrgUnit orgUnit : orgUnitList) {
            orgUnitDtos.add(orgUnit.toDto());
        }

        return orgUnitDtos;
    }

    private List<TrackedEntityAttributeDto> attributesToDtos(List<TrackedEntityAttribute> trackedEntityAttributeList) {
        List<TrackedEntityAttributeDto> trackedEntityAttributeDtos = new ArrayList<>();

        for (TrackedEntityAttribute trackedEntityAttribute : trackedEntityAttributeList) {
            trackedEntityAttributeDtos.add(trackedEntityAttribute.toDto());
        }

        return trackedEntityAttributeDtos;
    }

    private List<TrackedEntityDto> trackedEntitiesToDtos(List<TrackedEntity> trackedEntitiesList) {
        List<TrackedEntityDto> trackedEntityDtos = new ArrayList<>();

        for (TrackedEntity trackedEntity : trackedEntitiesList) {
            trackedEntityDtos.add(trackedEntity.toDto());
        }

        return trackedEntityDtos;
    }

    private List<ProgramDto> programsToDtos(List<Program> programList) {
        List<ProgramDto> programDtos = new ArrayList<>();

        for (Program program : programList) {
            programDtos.add(program.toDto());
        }

        return programDtos;
    }
}
