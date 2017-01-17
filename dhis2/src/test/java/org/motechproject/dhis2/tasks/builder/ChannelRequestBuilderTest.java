package org.motechproject.dhis2.tasks.builder;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.dhis2.domain.DataSet;
import org.motechproject.dhis2.domain.Program;
import org.motechproject.dhis2.domain.Stage;
import org.motechproject.dhis2.domain.TrackedEntity;
import org.motechproject.dhis2.domain.TrackedEntityAttribute;
import org.motechproject.dhis2.event.EventSubjects;
import org.motechproject.dhis2.rest.domain.ServerVersion;
import org.motechproject.dhis2.rest.service.DhisWebService;
import org.motechproject.dhis2.service.DataSetService;
import org.motechproject.dhis2.service.ProgramService;
import org.motechproject.dhis2.service.StageService;
import org.motechproject.dhis2.service.TrackedEntityAttributeService;
import org.motechproject.dhis2.service.TrackedEntityService;
import org.motechproject.dhis2.tasks.DisplayNames;
import org.motechproject.dhis2.util.DummyData;
import org.motechproject.tasks.contract.ActionEventRequest;
import org.motechproject.tasks.contract.ChannelRequest;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Version;

import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ChannelRequestBuilderTest {

    public static final String TEST_VERSION = "2.22";

    @Mock
    private ProgramService programService;

    @Mock
    private StageService stageService;

    @Mock
    private DataSetService dataSetService;

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

    @Mock
    private DhisWebService dhisWebService;

    @Mock
    private ProgramActionBuilder programActionBuilder;

    @Mock
    private CreateInstanceActionBuilder createInstanceActionBuilder;

    @Mock
    private SendDataValueSetActionBuilder sendDataValueSetActionBuilder;

    @Mock
    private StageActionBuilder stageActionBuilder;

    @InjectMocks
    private ChannelRequestBuilder builder = new ChannelRequestBuilder();

    private ServerVersion serverVersion = new ServerVersion(TEST_VERSION);

    private List<Program> programs;
    private List<Stage> stages;
    private List<TrackedEntityAttribute> attributes;
    private List<TrackedEntity> trackedEntities;
    private List<DataSet> dataSets;

    private ChannelRequest request;
    private static final int EXPECTED_ACTIONS_SIZE = 11;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        mockTestData();
        mockActionEventRequest();

        request = builder.build();
    }

    @Test
    public void testBuildChannelRequest() throws Exception {
        assertNotNull(request);
        Assert.assertEquals(request.getDisplayName(), DisplayNames.DHIS2_DISPLAY_NAME);
        assertEquals(EXPECTED_ACTIONS_SIZE, request.getActionTaskEvents().size());
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

    private void mockTestData() {
        programs = DummyData.preparePrograms();
        stages = DummyData.prepareStages();
        attributes = DummyData.prepareTrackedEntityAttributes();
        dataSets = DummyData.prepareDataSets();
        trackedEntities = DummyData.prepareTrackedEntities();

        when(programService.findByRegistration(true)).thenReturn(programs);
        when(stageService.findAll()).thenReturn(stages);
        when(trackedEntityAttributeService.findAll()).thenReturn(attributes);
        when(trackedEntityService.findAll()).thenReturn(trackedEntities);
        when(dataSetService.findAll()).thenReturn(dataSets);

        when(bundleContext.getBundle()).thenReturn(bundle);
        when(bundle.getVersion()).thenReturn(version);
        when(dhisWebService.getServerVersion()).thenReturn(serverVersion);

        when(bundle.getSymbolicName()).thenReturn("BundleSymbolicName");
        when(version.toString()).thenReturn("bundleVersion");
    }

    private void mockActionEventRequest() {
        when(stageActionBuilder.build(stages)).thenReturn(prepareStagesActionEventRequests());
        when(createInstanceActionBuilder.build(attributes, trackedEntities)).thenReturn(prepareInstanceActionEventRequests());
        when(programActionBuilder.build(programs, serverVersion)).thenReturn(prepareProgramActionEventRequests());
        when(sendDataValueSetActionBuilder.addSendDataValueSetActions(dataSets)).thenReturn(prepareDataSetsActionEventRequests());
    }

    private List<ActionEventRequest> prepareProgramActionEventRequests() {
        ProgramActionBuilder builder = new ProgramActionBuilder();
        List<ActionEventRequest> result = builder.build(programs, serverVersion);

        return result;
    }

    private List<ActionEventRequest> prepareStagesActionEventRequests() {
        StageActionBuilder builder = new StageActionBuilder();
        List<ActionEventRequest> result = builder.build(stages);

        return result;
    }

    private List<ActionEventRequest> prepareInstanceActionEventRequests() {
        CreateInstanceActionBuilder builder = new CreateInstanceActionBuilder();
        List<ActionEventRequest> result = builder.build(attributes, trackedEntities);

        return result;
    }

    private List<ActionEventRequest> prepareDataSetsActionEventRequests() {
        SendDataValueSetActionBuilder builder = new SendDataValueSetActionBuilder();
        List<ActionEventRequest> result = builder.addSendDataValueSetActions(dataSets);

        return result;
    }
}
