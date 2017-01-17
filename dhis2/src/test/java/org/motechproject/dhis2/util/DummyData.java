package org.motechproject.dhis2.util;

import org.motechproject.dhis2.domain.DataElement;
import org.motechproject.dhis2.domain.DataSet;
import org.motechproject.dhis2.domain.Program;
import org.motechproject.dhis2.domain.Stage;
import org.motechproject.dhis2.domain.TrackedEntity;
import org.motechproject.dhis2.domain.TrackedEntityAttribute;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class responsible for providing dummy data used by tests.
 */
public final class DummyData {

    /**
     * Prepares a list of data sets;
     */
    public static List<DataSet> prepareDataSets() {
        List<DataSet> dataSets = new ArrayList<>();

        List<DataElement> dataElements = new ArrayList<>();
        dataElements.add(new DataElement("dataElement1", "dataElement1Id"));
        dataElements.add(new DataElement("dataElement2", "dataElement2Id"));

        DataSet dataSet = new DataSet();
        dataSet.setName("dataSet1");
        dataSet.setUuid("dataSet1Id");
        dataSet.setDataElementList(dataElements);

        dataSets.add(dataSet);

        dataElements = new ArrayList<>();
        dataElements.add(new DataElement("dataElement3", "dataElement3Id"));
        dataElements.add(new DataElement("dataElement4", "dataElement4Id"));

        dataSet = new DataSet();
        dataSet.setName("dataSet2");
        dataSet.setUuid("dataSet2Id");
        dataSet.setDataElementList(dataElements);

        dataSets.add(dataSet);

        return dataSets;
    }

    /**
     * Prepares a list of programs;
     */
    public static List<Program> preparePrograms() {
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
        program2.setName("Program2");
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

    /**
     * Prepares a list of stages;
     */
    public static List<Stage> prepareStages() {
        List<Stage> stages = new ArrayList<>();

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

        return stages;
    }

    /**
     * Prepares a list of tracked entities;
     */
    public static List<TrackedEntity> prepareTrackedEntities() {
        List<TrackedEntity> trackedEntities = new ArrayList<>();

        TrackedEntity trackedEntity1 = new TrackedEntity("trackedEntity1", "ID1");
        TrackedEntity trackedEntity2 = new TrackedEntity("trackedEntity2", "ID2");

        trackedEntities.add(trackedEntity1);
        trackedEntities.add(trackedEntity2);

        return trackedEntities;
    }

    /**
     * Prepares a list of tracked entity attributes;
     */
    public static List<TrackedEntityAttribute> prepareTrackedEntityAttributes() {
        List<TrackedEntityAttribute> attributes = new ArrayList<>();

        TrackedEntityAttribute attribute1 = new TrackedEntityAttribute("attribute1", "ID1");
        TrackedEntityAttribute attribute2 = new TrackedEntityAttribute("attribute2", "ID2");

        attributes.add(attribute1);
        attributes.add(attribute2);

        return attributes;
    }

    /**
     * Utility class, should not be initiated.
     */
    private DummyData() {
    }

}
