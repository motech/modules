package org.motechproject.dhis2.util;

import org.motechproject.dhis2.domain.DataElement;
import org.motechproject.dhis2.domain.DataSet;

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
     * Utility class, should not be initiated.
     */
    private DummyData() {
    }

}
