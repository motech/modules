package org.motechproject.ivr.event;

import org.junit.Test;
import org.motechproject.ivr.domain.CallDetailRecord;
import org.motechproject.ivr.domain.CallDirection;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * EventParams Unit Tests
 */
public class EventParamsTest {
    @Test
    public void verifyFunctional() {
        //Construct a CDR
        Map<String, String> extraData = new HashMap<>();
        extraData.put("foo", "bar");
        CallDetailRecord callDetailRecord = new CallDetailRecord("config", "sometime", "from", "to",
                CallDirection.INBOUND, "answered", null, "motechCallId", "providerCallId", extraData);

        //Pass service to eventParamsFromCallDetailRecord
        Map<String, Object> eventParams = EventParams.eventParamsFromCallDetailRecord(callDetailRecord);

        //Verify all data was passed properly
        assertEquals("config", eventParams.get(EventParams.CONFIG));
        assertEquals("sometime", eventParams.get(EventParams.PROVIDER_TIMESTAMP));
        assertEquals("from", eventParams.get(EventParams.FROM));
        assertEquals("to", eventParams.get(EventParams.TO));
        assertEquals(CallDirection.INBOUND, eventParams.get(EventParams.CALL_DIRECTION));
        assertEquals("answered", eventParams.get(EventParams.CALL_STATUS));
        assertEquals("motechCallId", eventParams.get(EventParams.MOTECH_CALL_ID));
        assertEquals("providerCallId", eventParams.get(EventParams.PROVIDER_CALL_ID));
        assertEquals(extraData, eventParams.get(EventParams.PROVIDER_EXTRA_DATA));
        assertEquals(10, eventParams.size());
    }
}
