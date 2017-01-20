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

        dataSets.add(new DataSet("dataSet1Id", "dataSet1", dataElements));

        dataElements = new ArrayList<>();
        dataElements.add(new DataElement("dataElement3", "dataElement3Id"));
        dataElements.add(new DataElement("dataElement4", "dataElement4Id"));

        dataSets.add(new DataSet("dataSet2Id", "dataSet2", dataElements));

        return dataSets;
    }

    /**
     * Prepares a list of programs;
     */
    public static List<Program> preparePrograms() {
        List<Program> programs = new ArrayList<>();
        List<Stage> stages = new ArrayList<>();
        List<TrackedEntityAttribute> attributes = new ArrayList<>();

        attributes.add(new TrackedEntityAttribute("attribute1", "uuid"));
        attributes.add(new TrackedEntityAttribute("attribute2", "uuid"));

        TrackedEntity trackedEntity = new TrackedEntity("trackedEntityName","trackedEntityID");

        programs.add(new Program("program1UUID", "Program1", trackedEntity, stages, attributes, false, true, null));
        programs.add(new Program("program2UUID", "Program2", trackedEntity, stages, attributes, false, true, null));

        return programs;
    }

    /**
     * Prepares a list of stages;
     */
    public static List<Stage> prepareStages() {
        List<Stage> stages = new ArrayList<>();
        List<DataElement> dataElements = new ArrayList<>();

        dataElements.add(new DataElement("dataElementName1","dataElementID1"));
        dataElements.add(new DataElement("dataElementName2","dataElementID2"));

        stages.add(new Stage("stageID1", "stage1", dataElements, "programID", true));
        stages.add(new Stage("stageID2", "stage2", dataElements, "programID", true));

        return stages;
    }

    /**
     * Prepares a list of tracked entities;
     */
    public static List<TrackedEntity> prepareTrackedEntities() {
        List<TrackedEntity> trackedEntities = new ArrayList<>();

        trackedEntities.add(new TrackedEntity("trackedEntity1", "ID1"));
        trackedEntities.add(new TrackedEntity("trackedEntity2", "ID2"));

        return trackedEntities;
    }

    /**
     * Prepares a list of tracked entity attributes;
     */
    public static List<TrackedEntityAttribute> prepareTrackedEntityAttributes() {
        List<TrackedEntityAttribute> attributes = new ArrayList<>();

        attributes.add(new TrackedEntityAttribute("attribute1", "ID1"));
        attributes.add(new TrackedEntityAttribute("attribute2", "ID2"));

        return attributes;
    }

    /**
     * Utility class, should not be initiated.
     */
    private DummyData() {
    }

}
