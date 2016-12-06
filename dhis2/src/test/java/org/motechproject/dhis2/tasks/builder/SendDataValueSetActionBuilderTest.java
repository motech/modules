package org.motechproject.dhis2.tasks.builder;

import org.junit.Test;
import org.motechproject.dhis2.domain.DataElement;
import org.motechproject.dhis2.domain.DataSet;
import org.motechproject.dhis2.event.EventParams;
import org.motechproject.dhis2.event.EventSubjects;
import org.motechproject.dhis2.tasks.DisplayNames;
import org.motechproject.dhis2.util.DummyData;
import org.motechproject.tasks.contract.ActionEventRequest;
import org.motechproject.tasks.contract.ActionParameterRequest;
import org.motechproject.tasks.domain.enums.MethodCallManner;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class SendDataValueSetActionBuilderTest {

    private SendDataValueSetActionBuilder sendDataValueSetActionBuilder = new SendDataValueSetActionBuilder();

    @Test
    public void shouldAddSendDataValueSetActions() {
        List<DataSet> dataSets = DummyData.prepareDataSets();

        List<ActionEventRequest> actual = sendDataValueSetActionBuilder.addSendDataValueSetActions(dataSets);

        assertThat(actual, equalTo(prepareActionEventRequests(dataSets)));
    }

    private List<ActionEventRequest> prepareActionEventRequests(List<DataSet> dataSets) {
        List<ActionEventRequest> requests = new ArrayList<>();

        for (DataSet dataSet : dataSets) {
            int order = 0;

            SortedSet<ActionParameterRequest> actionParameters = new TreeSet<>();

            actionParameters.add(new ActionParameterRequest(order++, EventParams.DATA_SET, null,
                    DisplayNames.DATA_SET, null, false, false, new TreeSet<>()));

            actionParameters.add(new ActionParameterRequest(order++, EventParams.COMPLETE_DATE, null,
                    DisplayNames.COMPLETE_DATE, null, false, false, new TreeSet<>()));

            actionParameters.add(new ActionParameterRequest(order++, EventParams.PERIOD, null, DisplayNames.PERIOD,
                    null, true, false, new TreeSet<>()));

            actionParameters.add(new ActionParameterRequest(order++, EventParams.LOCATION, null, DisplayNames.ORG_UNIT,
                    null, true, false, new TreeSet<>()));

            actionParameters.add(new ActionParameterRequest(order++, EventParams.CATEGORY_OPTION_COMBO, null,
                    DisplayNames.CATEGORY_OPTION_COMBO, null, false, false, new TreeSet<>()));

            actionParameters.add(new ActionParameterRequest(order++, EventParams.COMMENT, null, DisplayNames.COMMENT,
                    null, false, false, new TreeSet<>()));

            actionParameters.add(new ActionParameterRequest(order++, EventParams.ATTRIBUTE_OPTION_COMBO, null,
                    DisplayNames.ATTRIBUTE_OPTION_COMBO, null, false, false, new TreeSet<>()));

            for (DataElement dataElement : dataSet.getDataElementList()) {
                actionParameters.add(new ActionParameterRequest(order++, dataElement.getUuid(), null,
                        dataElement.getName(), null, false, false, new TreeSet<>()));
            }

            actionParameters.add(new ActionParameterRequest(order, dataSet.getUuid(), null, DisplayNames.DATA_SET,
                    null, false, true, new TreeSet<>()));

            requests.add(new ActionEventRequest(
                    String.format("%s [%s]", DisplayNames.SEND_DATA_VALUE_SET, dataSet.getName()),
                    String.format("%s [%s]", DisplayNames.SEND_DATA_VALUE_SET, dataSet.getName()),
                    EventSubjects.SEND_DATA_VALUE_SET,
                    null,
                    ChannelRequestBuilder.ACTION_PROXY_SERVICE,
                    "sendDataValueSet",
                    MethodCallManner.MAP.name(),
                    actionParameters));
        }

        return requests;
    }
}