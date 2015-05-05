package org.motechproject.dhis2.tasks;

import org.junit.Test;
import org.motechproject.dhis2.domain.DataElement;
import org.motechproject.dhis2.domain.Stage;
import org.motechproject.dhis2.event.EventParams;
import org.motechproject.dhis2.event.EventSubjects;
import org.motechproject.tasks.contract.ActionEventRequest;
import org.motechproject.tasks.contract.ActionParameterRequest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class StageActionBuilderTest {

    private StageActionBuilder stageActionBuilder;

    @Test
    public void testBuildStagesWithDataElements() throws Exception{

        stageActionBuilder = new StageActionBuilder();
        List<Stage> stages = new ArrayList<>();

        DataElement dataElement1 = new DataElement("dataElementName1","dataElementID1");
        DataElement dataElement2 = new DataElement("dataElementName2","dataElementID2");

        Stage stage1 = new Stage();
        stage1.setName("stage1");
        stage1.setProgram("programID");
        stage1.setUuid("stageID");
        stage1.setRegistration(true);

        List<DataElement> dataElements = new ArrayList<>();
        dataElements.add(dataElement1);
        dataElements.add(dataElement2);

        stage1.setDataElements(dataElements);

        Stage stage2 = new Stage();
        stage2.setName("stage2");
        stage2.setProgram("programID");
        stage2.setUuid("stageID");
        stage2.setRegistration(false);

        stage2.setDataElements(dataElements);

        stages.add(stage1);
        stages.add(stage2);

        List<ActionEventRequest> actionEventRequests = stageActionBuilder.build(stages);

        assertNotNull(actionEventRequests);
        assertEquals(actionEventRequests.size(),2);

        ActionEventRequest eventRequest = actionEventRequests.get(0);
        assertEquals(eventRequest.getSubject(), EventSubjects.UPDATE_PROGRAM_STAGE);
        assertEquals(eventRequest.getName(),stage1.getName());
        assertEquals(eventRequest.getDisplayName(),DisplayNames.STAGE_EVENT + " [" + stage1.getName() + "]");

        SortedSet<ActionParameterRequest> actionParameters = eventRequest.getActionParameters();

        Iterator<ActionParameterRequest> itr = actionParameters.iterator();
        ActionParameterRequest request = itr.next();

        assertEquals(request.getDisplayName(), DisplayNames.EXTERNAL_ID);
        assertEquals(request.getKey(), EventParams.EXTERNAL_ID);

        request = itr.next();
        assertEquals(request.getKey(),EventParams.REGISTRATION);
        assertEquals(request.getDisplayName(),EventParams.REGISTRATION);

        request = itr.next();
        assertEquals(request.getKey(),EventParams.PROGRAM);
        assertEquals(request.getValue(), stage1.getProgram());

        request = itr.next();
        assertEquals(request.getKey(),EventParams.STAGE);
        assertEquals(request.getValue(),stage1.getUuid());

        request = itr.next();
        assertEquals(request.getDisplayName(),DisplayNames.EVENT_DATE);

        request = itr.next();
        assertEquals(request.getDisplayName(),DisplayNames.ORG_UNIT);

        request = itr.next();
        assertEquals(request.getDisplayName(),dataElement1.getName());
        assertNull(request.getValue());



    }

    @Test
    public void testBuildStagesWithoutRegistration () throws Exception {

        stageActionBuilder = new StageActionBuilder();
        List<Stage> stages = new ArrayList<>();

        DataElement dataElement1 = new DataElement("dataElementName1","dataElementID1");
        DataElement dataElement2 = new DataElement("dataElementName2","dataElementID2");

        List<DataElement> dataElements = new ArrayList<>();
        dataElements.add(dataElement1);
        dataElements.add(dataElement2);

        Stage stage2 = new Stage();
        stage2.setName("stage2");
        stage2.setProgram("programID");
        stage2.setUuid("stageID");
        stage2.setRegistration(false);

        stage2.setDataElements(dataElements);

        stages.add(stage2);

        List<ActionEventRequest> actionEventRequests = stageActionBuilder.build(stages);

        assertNotNull(actionEventRequests);
        assertEquals(actionEventRequests.size(),1);


        ActionEventRequest eventRequest = actionEventRequests.get(0);
        assertEquals(eventRequest.getSubject(), EventSubjects.UPDATE_PROGRAM_STAGE);
        assertEquals(eventRequest.getName(),stage2.getName());
        assertEquals(eventRequest.getDisplayName(),DisplayNames.STAGE_EVENT + " [" + stage2.getName() + "]");

        SortedSet<ActionParameterRequest> actionParameters = eventRequest.getActionParameters();

        Iterator<ActionParameterRequest> itr = actionParameters.iterator();
        ActionParameterRequest request = itr.next();
        assertEquals(request.getKey(),EventParams.REGISTRATION);
        assertEquals(request.getDisplayName(),EventParams.REGISTRATION);

        request = itr.next();
        assertEquals(request.getKey(),EventParams.PROGRAM);
        assertEquals(request.getValue(), stage2.getProgram());

        request = itr.next();
        assertEquals(request.getKey(),EventParams.STAGE);
        assertEquals(request.getValue(),stage2.getUuid());

        request = itr.next();
        assertEquals(request.getDisplayName(),DisplayNames.EVENT_DATE);

        request = itr.next();
        assertEquals(request.getDisplayName(),DisplayNames.ORG_UNIT);

        request = itr.next();
        assertEquals(request.getDisplayName(),dataElement1.getName());
        assertNull(request.getValue());


    }
}
