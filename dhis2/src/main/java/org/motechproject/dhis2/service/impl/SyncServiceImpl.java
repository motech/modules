package org.motechproject.dhis2.service.impl;

import org.motechproject.dhis2.domain.DataElement;
import org.motechproject.dhis2.domain.Program;
import org.motechproject.dhis2.domain.Stage;
import org.motechproject.dhis2.domain.TrackedEntity;
import org.motechproject.dhis2.domain.TrackedEntityAttribute;
import org.motechproject.dhis2.rest.domain.DataElementDto;
import org.motechproject.dhis2.rest.domain.OrganisationUnitDto;
import org.motechproject.dhis2.rest.domain.ProgramDto;
import org.motechproject.dhis2.rest.domain.ProgramStageDataElementDto;
import org.motechproject.dhis2.rest.domain.ProgramStageDto;
import org.motechproject.dhis2.rest.domain.ProgramTrackedEntityAttributeDto;
import org.motechproject.dhis2.rest.domain.TrackedEntityAttributeDto;
import org.motechproject.dhis2.rest.domain.TrackedEntityDto;
import org.motechproject.dhis2.service.DataElementService;
import org.motechproject.dhis2.rest.service.DhisWebService;
import org.motechproject.dhis2.service.OrgUnitService;
import org.motechproject.dhis2.service.ProgramService;
import org.motechproject.dhis2.service.StageService;
import org.motechproject.dhis2.service.SyncService;
import org.motechproject.dhis2.service.TrackedEntityAttributeService;
import org.motechproject.dhis2.service.TrackedEntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Implementation of {@link org.motechproject.dhis2.service.SyncService}
 */
@Service("syncService")
public class SyncServiceImpl implements SyncService {
    @Autowired
    private DhisWebService dhisWebService;

    @Autowired
    private DataElementService dataElementService;

    @Autowired
    private TrackedEntityAttributeService trackedEntityAttributeService;

    @Autowired
    private TrackedEntityService trackedEntityService;

    @Autowired
    private ProgramService programService;

    @Autowired
    private StageService stageService;

    @Autowired
    private OrgUnitService orgUnitService;

    private static final Logger LOGGER = LoggerFactory.getLogger(SyncServiceImpl.class);

    @Override
    @Transactional
    public boolean sync() {
        LOGGER.debug("Starting Sync");
        try {
            long startTime = System.nanoTime();
            dropExistingData();
            addDataElements();
            addAttributes();
            addTrackedEntities();
            addPrograms();
            addOrgUnits();

            long endTime = System.nanoTime();

            LOGGER.debug("Sync successful.");
            LOGGER.debug("Time for sync: " + TimeUnit.SECONDS.convert(endTime - startTime,
                    TimeUnit.NANOSECONDS) + "seconds");
            return true;
        } catch (Exception e) {
            LOGGER.error("Problem with DHIS2 application Schema. Sync unsuccessful.", e);
            return false;
        }
    }

    /*
     * Request data elements from DHIS and persist in MDS. The partial data element objects returned by the top-level api
     * endpoint suffice for current needs.
     */
    private void addDataElements() {
        List<DataElementDto> dataElementDtos = dhisWebService.getDataElements();

        for (DataElementDto dataElementDto : dataElementDtos) {
            dataElementService.createFromDetails(dataElementDto);
        }
    }

    /*
     * Request tracked entity attributes from DHIS and persist in MDS. The partial tracked entity attribute objects returned by
     * the top-level api endpoint suffice for current needs.
     */
    private void addAttributes()  {
        List<TrackedEntityAttributeDto> trackedEntityAttributeDtos = dhisWebService.getTrackedEntityAttributes();
        for (TrackedEntityAttributeDto trackedEntityAttributeDto : trackedEntityAttributeDtos) {
            trackedEntityAttributeService.createFromDetails(trackedEntityAttributeDto);
        }
    }

    /*
     * Request tracked entities from DHIS and persist in MDS. The partial tracked entity objects returned by the top-level
     * api endpoint suffice for current needs.
     */
    private void addTrackedEntities() {
        List<TrackedEntityDto> trackedEntityDtos = dhisWebService.getTrackedEntities();
        for (TrackedEntityDto trackedEntityDto : trackedEntityDtos) {
            trackedEntityService.createFromDetails(trackedEntityDto);
        }
    }

    /*
     * Request programs from DHIS and persist in MDS. Since we need data from full program objects, we load the partial
     * objects from the top-level endpoints and then iterate through the links for the complete objects.
     */
    private void addPrograms() {
        List<ProgramDto> partialDtos = dhisWebService.getPrograms();

        for (ProgramDto partialDto : partialDtos) {
            ProgramDto fullDto = dhisWebService.getProgramByHref(partialDto.getHref());
            Program program = programService.createFromDetails(fullDto);

            /**
             * Request and add the program's sub-objects (tracked entity, program stages, program tracked entity's attributes).
             */
            if (fullDto.getTrackedEntity() != null) {
                program.setTrackedEntity(getProgramTrackedEntityFromDto(fullDto.getTrackedEntity()));
            }

            if (fullDto.getProgramStages() != null) {
                program.setStages(getStagesFromDtos(fullDto.getProgramStages(), program.getUuid(), program.hasRegistration()));
            }

            if (fullDto.getProgramTrackedEntityAttributes() != null) {
                program.setAttributes(getTrackedEntityAttributesFromDtos(fullDto.getProgramTrackedEntityAttributes()));
            }

            programService.update(program);
        }
    }

    /*
     * Helper to load the program's tracked entity from MDS, if we've already persisted it, or make a request to DHIS and persist,
     * if we haven't.
     */
    private TrackedEntity getProgramTrackedEntityFromDto(TrackedEntityDto partialDto) {
        TrackedEntity trackedEntity = trackedEntityService.findById(partialDto.getId());
        if (trackedEntity == null) {
            TrackedEntityDto fullDto = dhisWebService.getTrackedEntityByHref(partialDto.getHref());
            trackedEntity = trackedEntityService.createFromDetails(fullDto);
        }
        return trackedEntity;
    }


    /*
     * Helper to load the program's program stages from MDS, if we've already persisted them, or make a request to DHIS, persist,
     * if we haven't.
     */
    private List<Stage> getStagesFromDtos(List<ProgramStageDto> partialStageDtos, String programId, boolean hasRegistration) {
        List<Stage> stages = new ArrayList<Stage>();

        for (ProgramStageDto partialStageDto : partialStageDtos) {
            Stage stage = stageService.findById(partialStageDto.getId());

            if (stage == null) {
                ProgramStageDto fullDto = dhisWebService.getProgramStageByHref(partialStageDto.getHref());
                stage = stageService.createFromDetails(fullDto, programId, hasRegistration);
                stage.setDataElements(getStageDataElementsFromDtos(fullDto.getProgramStageDataElements()));
            }
            stages.add(stage);
        }

        return stages;
    }

    /*
     * Helper to load the program stage's data elements from MDS, if we've already persisted them, or make a request to DHIS,
     * persist, if we haven't.
     */
    private List<DataElement> getStageDataElementsFromDtos(List<ProgramStageDataElementDto> programStageDataElementDtos) {
        List<DataElement> dataElements = new ArrayList<DataElement>();

        for (ProgramStageDataElementDto programStageDataElementDto : programStageDataElementDtos) {
            DataElementDto dataElementDto = programStageDataElementDto.getDataElement();
            DataElement dataElement = dataElementService.findById(dataElementDto.getId());
            if (dataElement == null) {
                dataElement = dataElementService.createFromDetails(dataElementDto);
            }
            dataElements.add(dataElement);
        }
        return dataElements;
    }


    /*
     * Helper to laod the program's program tracked entity attributes from MDS, if we've already persisted them, or make a
     * request to DHIS and persist, if we haven't.
     */
    private List<TrackedEntityAttribute> getTrackedEntityAttributesFromDtos(List<ProgramTrackedEntityAttributeDto> programTrackedEntityAttributeDtos) {
        List<TrackedEntityAttribute> trackedEntityAttributes = new ArrayList<TrackedEntityAttribute>();

        for (ProgramTrackedEntityAttributeDto programTrackedEntityAttributeDto : programTrackedEntityAttributeDtos) {
            TrackedEntityAttributeDto trackedEntityAttributeDto = programTrackedEntityAttributeDto.getTrackedEntityAttribute();
            TrackedEntityAttribute trackedEntityAttribute = trackedEntityAttributeService.findById(trackedEntityAttributeDto.getId());
            if (trackedEntityAttribute == null) {
                trackedEntityAttribute = trackedEntityAttributeService.createFromDetails(trackedEntityAttributeDto);
            }
            trackedEntityAttributes.add(trackedEntityAttribute);
        }
        return trackedEntityAttributes;
    }

    /*
     * Request organisation units from DHIS and persist in MDS. The partial organisation unit objects returned by the top-level
     * api endpoint suffice for current needs.
     */
    private void addOrgUnits() {
        List<OrganisationUnitDto> orgUnitDtos = dhisWebService.getOrganisationUnits();
        for (OrganisationUnitDto orgUnitDto : orgUnitDtos) {
            orgUnitService.createFromDetails(orgUnitDto);
        }
    }

    private void dropExistingData() {
        programService.deleteAll();
        trackedEntityAttributeService.deleteAll();
        trackedEntityService.deleteAll();
        orgUnitService.deleteAll();
        dataElementService.deleteAll();
        stageService.deleteAll();
    }
}
