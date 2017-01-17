package org.motechproject.dhis2.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.motechproject.dhis2.domain.DataElement;
import org.motechproject.dhis2.domain.Stage;
import org.motechproject.dhis2.domain.TrackedEntity;
import org.motechproject.dhis2.domain.TrackedEntityAttribute;
import org.motechproject.dhis2.rest.domain.DataElementDto;
import org.motechproject.dhis2.rest.domain.DataSetDto;
import org.motechproject.dhis2.rest.domain.OrganisationUnitDto;
import org.motechproject.dhis2.rest.domain.ProgramDto;
import org.motechproject.dhis2.rest.domain.ProgramStageDataElementDto;
import org.motechproject.dhis2.rest.domain.ProgramStageDto;
import org.motechproject.dhis2.rest.domain.ProgramTrackedEntityAttributeDto;
import org.motechproject.dhis2.rest.domain.TrackedEntityAttributeDto;
import org.motechproject.dhis2.rest.domain.TrackedEntityDto;
import org.motechproject.dhis2.rest.service.DhisWebService;
import org.motechproject.dhis2.service.DataElementService;
import org.motechproject.dhis2.service.DataSetService;
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

    @Autowired
    private DataSetService dataSetService;

    private static final Logger LOGGER = LoggerFactory.getLogger(SyncServiceImpl.class);

    @Override
    @Transactional
    public synchronized boolean sync() {
        LOGGER.debug("Starting Sync");
        try {
            long startTime = System.nanoTime();
            dropExistingData();
            addDataElements();
            addDataSets();
            addAttributes();
            addTrackedEntities();
            addPrograms();
            addOrgUnits();

            long endTime = System.nanoTime();

            LOGGER.debug("Sync successful.");
            LOGGER.debug("Time for sync: " + TimeUnit.SECONDS.convert(endTime - startTime,
                    TimeUnit.NANOSECONDS) + "seconds");
            return true;
        } catch (RuntimeException e) {
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
            if (StringUtils.isEmpty(dataElementDto.getName())) {
                dataElementService.createFromDetails(dhisWebService.getDataElementById(dataElementDto.getId()));
            } else {
                dataElementService.createFromDetails(dataElementDto);
            }
        }
    }

    private void addDataSets() {
        List<DataSetDto> dataSetDtos = dhisWebService.getDataSets();

        for (DataSetDto dataSetDto : dataSetDtos) {
            dataSetService.createFromDetails(dataSetDto);
        }
    }

    /*
     * Request tracked entity attributes from DHIS and persist in MDS. The partial tracked entity attribute objects returned by
     * the top-level api endpoint suffice for current needs.
     */
    private void addAttributes()  {
        List<TrackedEntityAttributeDto> trackedEntityAttributeDtos = dhisWebService.getTrackedEntityAttributes();
        for (TrackedEntityAttributeDto trackedEntityAttributeDto : trackedEntityAttributeDtos) {
            if (StringUtils.isEmpty(trackedEntityAttributeDto.getName())) {
                trackedEntityAttributeService.createFromDetails(dhisWebService.getTrackedEntityAttributeById(trackedEntityAttributeDto.getId()));
            } else {
                trackedEntityAttributeService.createFromDetails(trackedEntityAttributeDto);
            }
        }
    }

    /*
     * Request tracked entities from DHIS and persist in MDS. The partial tracked entity objects returned by the top-level
     * api endpoint suffice for current needs.
     */
    private void addTrackedEntities() {
        List<TrackedEntityDto> trackedEntityDtos = dhisWebService.getTrackedEntities();
        for (TrackedEntityDto trackedEntityDto : trackedEntityDtos) {
            if (StringUtils.isEmpty(trackedEntityDto.getName())) {
                trackedEntityService.createFromDetails(dhisWebService.getTrackedEntityById(trackedEntityDto.getId()));
            } else {
                trackedEntityService.createFromDetails(trackedEntityDto);
            }
        }
    }

    /*
     * Request programs from DHIS and persist in MDS. Since we need data from full program objects, we load the partial
     * objects from the top-level endpoints and then iterate through the links for the complete objects.
     */
    private void addPrograms() {
        List<ProgramDto> partialDtos = dhisWebService.getPrograms();

        for (ProgramDto partialDto : partialDtos) {
            ProgramDto fullDto;
            if (partialDto.getHref() == null) {
                fullDto = dhisWebService.getProgramById(partialDto.getId());
            } else {
                fullDto = dhisWebService.getProgramByHref(partialDto.getHref());
            }

            /**
             * Request and add the program's sub-objects (tracked entity, program stages, program tracked entity's attributes).
             */
            TrackedEntity trackedEntity = null;
            if (fullDto.getTrackedEntity() != null) {
                trackedEntity = getProgramTrackedEntityFromDto(fullDto.getTrackedEntity());
            }

            List<Stage> stages = null;
            if (fullDto.getProgramStages() != null) {
                stages = getStagesFromDtos(fullDto.getProgramStages(), fullDto.getId(), fullDto.getRegistration());
            }

            List<TrackedEntityAttribute> trackedEntityAttributes = null;
            if (fullDto.getProgramTrackedEntityAttributes() != null) {
                trackedEntityAttributes = getTrackedEntityAttributesFromDtos(fullDto.getProgramTrackedEntityAttributes());
            }

            programService.createFromDetails(fullDto, trackedEntity, stages, trackedEntityAttributes);
        }
    }

    /*
     * Helper to load the program's tracked entity from MDS, if we've already persisted it, or make a request to DHIS and persist,
     * if we haven't.
     */
    private TrackedEntity getProgramTrackedEntityFromDto(TrackedEntityDto partialDto) {
        TrackedEntity trackedEntity = trackedEntityService.findById(partialDto.getId());
        if (trackedEntity == null) {
            TrackedEntityDto fullDto;
            if (partialDto.getHref() == null) {
                fullDto = dhisWebService.getTrackedEntityById(partialDto.getId());
            } else {
                fullDto = dhisWebService.getTrackedEntityByHref(partialDto.getHref());
            }
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
                ProgramStageDto fullDto;
                if (partialStageDto.getHref() == null) {
                    fullDto = dhisWebService.getProgramStageById(partialStageDto.getId());
                } else {
                    fullDto = dhisWebService.getProgramStageByHref(partialStageDto.getHref());
                }
                stage = stageService.createFromDetails(fullDto, programId, hasRegistration, getStageDataElementsFromDtos(fullDto.getProgramStageDataElements()));
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
            if (programStageDataElementDto.getDataElement() == null && programStageDataElementDto.getId() != null) {
                programStageDataElementDto = dhisWebService.getProgramStageDataElementById(programStageDataElementDto.getId());
            }

            DataElementDto dataElementDto = programStageDataElementDto.getDataElement();
            if (dataElementDto.getId() != null) {
                dataElementDto = dhisWebService.getDataElementById(dataElementDto.getId());
            }
            if (dataElementDto != null) {
                DataElement dataElement = dataElementService.findById(dataElementDto.getId());
                if (dataElement == null) {
                    dataElement = dataElementService.createFromDetails(dataElementDto);
                }
                dataElements.add(dataElement);
            }
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
            if (programTrackedEntityAttributeDto.getTrackedEntityAttribute() == null && programTrackedEntityAttributeDto.getId() != null) {
                programTrackedEntityAttributeDto = dhisWebService.getProgramTrackedEntityAttributeById(programTrackedEntityAttributeDto.getId());
            }
            TrackedEntityAttributeDto trackedEntityAttributeDto = programTrackedEntityAttributeDto.getTrackedEntityAttribute();
            TrackedEntityAttribute trackedEntityAttribute = trackedEntityAttributeService.findById(trackedEntityAttributeDto.getId());
            if (trackedEntityAttribute == null) {
                if (programTrackedEntityAttributeDto.getId() != null) {
                    trackedEntityAttributeDto = dhisWebService.getTrackedEntityAttributeById(programTrackedEntityAttributeDto.getId());
                }
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
            if (StringUtils.isEmpty(orgUnitDto.getName())) {
                orgUnitService.createFromDetails(dhisWebService.getOrganisationUnitById(orgUnitDto.getId()));
            } else {
                orgUnitService.createFromDetails(orgUnitDto);
            }
        }
    }

    private void dropExistingData() {
        dataSetService.deleteAll();
        programService.deleteAll();
        trackedEntityAttributeService.deleteAll();
        trackedEntityService.deleteAll();
        orgUnitService.deleteAll();
        dataElementService.deleteAll();
        stageService.deleteAll();
    }
}
