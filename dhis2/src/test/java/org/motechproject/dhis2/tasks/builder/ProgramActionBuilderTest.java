package org.motechproject.dhis2.tasks.builder;

import org.junit.Test;
import org.motechproject.dhis2.domain.Program;
import org.motechproject.dhis2.domain.Stage;
import org.motechproject.dhis2.domain.TrackedEntity;
import org.motechproject.dhis2.domain.TrackedEntityAttribute;
import org.motechproject.dhis2.event.EventParams;
import org.motechproject.dhis2.event.EventSubjects;
import org.motechproject.dhis2.rest.domain.ServerVersion;
import org.motechproject.dhis2.tasks.DisplayNames;
import org.motechproject.dhis2.tasks.builder.ProgramActionBuilder;
import org.motechproject.tasks.contract.ActionEventRequest;
import org.motechproject.tasks.contract.ActionParameterRequest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ProgramActionBuilderTest {

    @Test
    public void shouldBuildProgramEnrollmentTaskActions() throws Exception {

        ProgramActionBuilder builder = new ProgramActionBuilder();
        List<Program> programs = prepareTestPrograms();

        Program program1 = programs.get(0);

        List<ActionEventRequest> actionEventRequests = builder.build(programs, new ServerVersion(ServerVersion.V2_18));

        assertNotNull(actionEventRequests);

        ActionEventRequest request = actionEventRequests.get(0);
        assertEquals(request.getSubject(), EventSubjects.ENROLL_IN_PROGRAM);
        assertEquals(request.getName(),program1.getName());
        assertEquals(request.getDisplayName(), DisplayNames.PROGRAM_ENROLLMENT + " [" + program1.getName() + "]");

        SortedSet<ActionParameterRequest> actionParameters = request.getActionParameters();

        Iterator<ActionParameterRequest> itr = actionParameters.iterator();
        ActionParameterRequest parameterRequest = itr.next();
        assertEquals(parameterRequest.getDisplayName(),DisplayNames.EXTERNAL_ID);
        assertEquals(parameterRequest.getKey(),EventParams.EXTERNAL_ID);

        parameterRequest = itr.next();
        assertEquals(parameterRequest.getDisplayName(),DisplayNames.ENROLLMENT_DATE);
        assertEquals(parameterRequest.getKey(),EventParams.DATE);

        parameterRequest = itr.next();
        assertEquals(parameterRequest.getDisplayName(),DisplayNames.PROGRAM_NAME);
        assertEquals(parameterRequest.getKey(),EventParams.PROGRAM);
        assertEquals(parameterRequest.getValue(),"program1UUID");

        parameterRequest = itr.next();
        assertEquals(parameterRequest.getDisplayName(), program1.getAttributes().get(0).getName());
        assertEquals(parameterRequest.getKey(), program1.getAttributes().get(0).getUuid());
        assertNull(parameterRequest.getValue());
    }

    @Test
    public void shouldBuildProgramEnrollmentTaskActionsWithOrgUnitForDhis219OrAbove() throws Exception {

        ProgramActionBuilder builder = new ProgramActionBuilder();
        List<Program> programs = prepareTestPrograms();

        Program program1 = programs.get(0);

        List<ActionEventRequest> actionEventRequests = builder.build(programs, new ServerVersion(ServerVersion.V2_19));

        assertNotNull(actionEventRequests);

        ActionEventRequest request = actionEventRequests.get(0);
        assertEquals(request.getSubject(), EventSubjects.ENROLL_IN_PROGRAM);
        assertEquals(request.getName(),program1.getName());
        assertEquals(request.getDisplayName(),DisplayNames.PROGRAM_ENROLLMENT + " [" + program1.getName() + "]");

        SortedSet<ActionParameterRequest> actionParameters = request.getActionParameters();

        Iterator<ActionParameterRequest> itr = actionParameters.iterator();
        ActionParameterRequest parameterRequest = itr.next();
        assertEquals(parameterRequest.getDisplayName(),DisplayNames.EXTERNAL_ID);
        assertEquals(parameterRequest.getKey(),EventParams.EXTERNAL_ID);

        parameterRequest = itr.next();
        assertEquals(parameterRequest.getDisplayName(),DisplayNames.ENROLLMENT_DATE);
        assertEquals(parameterRequest.getKey(),EventParams.DATE);

        parameterRequest = itr.next();
        assertEquals(parameterRequest.getDisplayName(),DisplayNames.PROGRAM_NAME);
        assertEquals(parameterRequest.getKey(),EventParams.PROGRAM);
        assertEquals(parameterRequest.getValue(),"program1UUID");

        parameterRequest = itr.next();
        assertEquals(parameterRequest.getDisplayName(), DisplayNames.ORG_UNIT);
        assertEquals(parameterRequest.getKey(), EventParams.LOCATION);
        assertNull(parameterRequest.getValue());

        parameterRequest = itr.next();
        assertEquals(parameterRequest.getDisplayName(), program1.getAttributes().get(0).getName());
        assertEquals(parameterRequest.getKey(), program1.getAttributes().get(0).getUuid());
        assertNull(parameterRequest.getValue());
    }

    @Test
    public void shouldBuildCreateAndEnrollTaskActions() throws Exception {

        ProgramActionBuilder builder = new ProgramActionBuilder();
        List<Program> programs = prepareTestPrograms();

        Program program1 = programs.get(0);

        List<ActionEventRequest> actionEventRequests = builder.build(programs, new ServerVersion(ServerVersion.V2_18));

        assertNotNull(actionEventRequests);

        ActionEventRequest request = actionEventRequests.get(1);
        assertEquals(request.getSubject(), EventSubjects.CREATE_AND_ENROLL);
        assertEquals(request.getName(),program1.getTrackedEntity().getName() + ", " + program1.getName());
        assertEquals(request.getDisplayName(),DisplayNames.CREATE_TRACKED_ENTITY_INSTANCE +
                " [" + program1.getTrackedEntity().getName() + "]" + " and " +
                DisplayNames.PROGRAM_ENROLLMENT + " [" + program1.getName() + "]");

        SortedSet<ActionParameterRequest> actionParameters = request.getActionParameters();

        Iterator<ActionParameterRequest> itr = actionParameters.iterator();
        ActionParameterRequest parameterRequest = itr.next();
        assertEquals(parameterRequest.getDisplayName(),DisplayNames.EXTERNAL_ID);
        assertEquals(parameterRequest.getKey(),EventParams.EXTERNAL_ID);

        parameterRequest = itr.next();
        assertEquals(parameterRequest.getDisplayName(),DisplayNames.ENROLLMENT_DATE);
        assertEquals(parameterRequest.getKey(),EventParams.DATE);

        parameterRequest = itr.next();
        assertEquals(parameterRequest.getDisplayName(),DisplayNames.PROGRAM_NAME);
        assertEquals(parameterRequest.getKey(),EventParams.PROGRAM);
        assertEquals(parameterRequest.getValue(),"program1UUID");

        parameterRequest = itr.next();
        assertEquals(parameterRequest.getDisplayName(), DisplayNames.ORG_UNIT);
        assertEquals(parameterRequest.getKey(), EventParams.LOCATION);
        assertNull(parameterRequest.getValue());

        parameterRequest = itr.next();
        assertEquals(parameterRequest.getDisplayName(),program1.getAttributes().get(0).getName());
        assertEquals(parameterRequest.getKey(),program1.getAttributes().get(0).getUuid());
        assertNull(parameterRequest.getValue());
    }

    private List<Program> prepareTestPrograms() {

        List<Program> programs = new ArrayList<>();
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
        program2.setName("program2");
        program2.setUuid("program2UUID");
        program2.setRegistration(true);
        program2.setSingleEvent(false);
        program2.setTrackedEntity(trackedEntity);
        program2.setStages(stages);
        program2.setAttributes(attributes);

        programs.add(program1);
        programs.add(program2);

        return programs;
    }

}
