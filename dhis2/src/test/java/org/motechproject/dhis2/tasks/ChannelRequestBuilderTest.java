package org.motechproject.dhis2.tasks;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.dhis2.domain.DataElement;
import org.motechproject.dhis2.domain.Program;
import org.motechproject.dhis2.domain.Stage;
import org.motechproject.dhis2.domain.TrackedEntity;
import org.motechproject.dhis2.domain.TrackedEntityAttribute;
import org.motechproject.dhis2.event.EventSubjects;
import org.motechproject.dhis2.service.ProgramService;
import org.motechproject.dhis2.service.StageService;
import org.motechproject.dhis2.service.TrackedEntityAttributeService;
import org.motechproject.dhis2.service.TrackedEntityService;
import org.motechproject.tasks.contract.ActionEventRequest;
import org.motechproject.tasks.contract.ChannelRequest;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import org.osgi.framework.Version;

@RunWith(MockitoJUnitRunner.class)
public class ChannelRequestBuilderTest {

    @Mock
    private ProgramService programService;

    @Mock
    private StageService stageService;

    @Mock
    private TrackedEntityService trackedEntityService;

    @Mock
    private TrackedEntityAttributeService trackedEntityAttributeService;

    @Mock
    private BundleContext bundleContext;

    @Mock
    private Bundle bundle;

    @Mock
    private Version version;

    private List<Program> programs;
    private List<Stage> stages;
    private List<TrackedEntityAttribute> attributes;
    private List<TrackedEntity> trackedEntities;

    private ChannelRequest request;

    @Before
    public void setup() {

        programs = new ArrayList<>();
        stages = new ArrayList<>();
        attributes = new ArrayList<>();
        trackedEntities = new ArrayList<>();

        setupStages();
        setupPrograms();
        setupTrackedEntities();

        when(programService.findByRegistration(true)).thenReturn(programs);
        when(stageService.findAll()).thenReturn(stages);
        when(trackedEntityAttributeService.findAll()).thenReturn(attributes);
        when(trackedEntityService.findAll()).thenReturn(trackedEntities);

        when(bundleContext.getBundle()).thenReturn(bundle);
        when(bundle.getVersion()).thenReturn(version);

        when(bundle.getSymbolicName()).thenReturn("BundleSymbolicName");
        when(version.toString()).thenReturn("bundleVersion");

        ChannelRequestBuilder builder = new ChannelRequestBuilder(bundleContext, programService, stageService,
                trackedEntityAttributeService, trackedEntityService);
        request = builder.build();

    }


    @Test
    public void testBuildChannelRequest() throws Exception {
        assertNotNull(request);
        assertEquals(request.getDisplayName(),DisplayNames.DHIS2_DISPLAY_NAME);
        assertEquals(request.getActionTaskEvents().size(),8);
    }

    @Test
    public void testActionEventRequests() throws Exception{
        List<ActionEventRequest> actionEventRequests = request.getActionTaskEvents();

        Iterator<ActionEventRequest> itr = actionEventRequests.iterator();

        ActionEventRequest actionEventRequest = itr.next();
        assertEquals(actionEventRequest.getName(),"Program1");
        assertEquals(actionEventRequest.getSubject(), EventSubjects.ENROLL_IN_PROGRAM);
        assertNotNull(actionEventRequest.getActionParameters());

        actionEventRequest = itr.next();
        assertEquals(actionEventRequest.getName(), "trackedEntityName, Program1");
        assertEquals(actionEventRequest.getSubject(), EventSubjects.CREATE_AND_ENROLL);
        assertNotNull(actionEventRequest.getActionParameters());


        actionEventRequest = itr.next();
        assertEquals(actionEventRequest.getName(),"Program2");
        assertEquals(actionEventRequest.getSubject(), EventSubjects.ENROLL_IN_PROGRAM);
        assertNotNull(actionEventRequest.getActionParameters());

        actionEventRequest = itr.next();
        assertEquals(actionEventRequest.getName(), "trackedEntityName, Program2");
        assertEquals(actionEventRequest.getSubject(), EventSubjects.CREATE_AND_ENROLL);
        assertNotNull(actionEventRequest.getActionParameters());


        actionEventRequest = itr.next();
        assertEquals(actionEventRequest.getName(),"stage1");
        assertEquals(actionEventRequest.getSubject(), EventSubjects.UPDATE_PROGRAM_STAGE);
        assertNotNull(actionEventRequest.getActionParameters());

        actionEventRequest = itr.next();
        assertEquals(actionEventRequest.getName(),"stage2");
        assertEquals(actionEventRequest.getSubject(), EventSubjects.UPDATE_PROGRAM_STAGE);
        assertNotNull(actionEventRequest.getActionParameters());


        actionEventRequest = itr.next();
        assertEquals(actionEventRequest.getName(),"trackedEntity1");
        assertEquals(actionEventRequest.getSubject(), EventSubjects.CREATE_ENTITY);
        assertNotNull(actionEventRequest.getActionParameters());

        actionEventRequest = itr.next();
        assertEquals(actionEventRequest.getName(),"trackedEntity2");
        assertEquals(actionEventRequest.getSubject(), EventSubjects.CREATE_ENTITY);
        assertNotNull(actionEventRequest.getActionParameters());


    }


    private void setupPrograms() {
        List<Stage> stages = new ArrayList<>();
        List<TrackedEntityAttribute> attributes = new ArrayList<>();

        TrackedEntityAttribute attribute1 = new TrackedEntityAttribute("attribute1", "uuid");
        TrackedEntityAttribute attribute2 = new TrackedEntityAttribute("attribute2", "uuid");
        attributes.add(attribute1);
        attributes.add(attribute2);

        TrackedEntity trackedEntity = new TrackedEntity("trackedEntityName","trackedEntityID");

        Program program1 = new Program();
        program1.setName("Program1");
        program1.setUuid("program1UUID");
        program1.setRegistration(true);
        program1.setSingleEvent(false);
        program1.setTrackedEntity(trackedEntity);
        program1.setStages(stages);
        program1.setAttributes(attributes);

        Program program2 = new Program();
        program2.setName("Program2");
        program2.setUuid("program2UUID");
        program2.setRegistration(true);
        program2.setSingleEvent(false);
        program2.setTrackedEntity(trackedEntity);
        program2.setStages(stages);
        program2.setAttributes(attributes);

        programs.add(program1);
        programs.add(program2);

    }

    private void setupStages() {

        DataElement dataElement1 = new DataElement("dataElementName1","dataElementID1");
        DataElement dataElement2 = new DataElement("dataElementName2","dataElementID2");

        Stage stage1 = new Stage();
        stage1.setName("stage1");
        stage1.setProgram("programID");
        stage1.setUuid("stageID");

        List<DataElement> dataElements = new ArrayList<>();
        dataElements.add(dataElement1);
        dataElements.add(dataElement2);

        stage1.setDataElements(dataElements);

        Stage stage2 = new Stage();
        stage2.setName("stage2");
        stage2.setProgram("programID");
        stage2.setUuid("stageID");

        stage2.setDataElements(dataElements);

        stages.add(stage1);
        stages.add(stage2);
    }

    private void setupTrackedEntities() {

        TrackedEntity trackedEntity1 = new TrackedEntity("trackedEntity1", "ID1");
        TrackedEntity trackedEntity2 = new TrackedEntity("trackedEntity2", "ID2");

        trackedEntities.add(trackedEntity1);
        trackedEntities.add(trackedEntity2);


        TrackedEntityAttribute attribute1 = new TrackedEntityAttribute("attribute1", "ID1");
        TrackedEntityAttribute attribute2 = new TrackedEntityAttribute("attribute2", "ID2");

        attributes.add(attribute1);
        attributes.add(attribute2);
    }


}
