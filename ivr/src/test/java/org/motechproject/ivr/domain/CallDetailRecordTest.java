package org.motechproject.ivr.domain;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * CallDetailRecord Unit Tests
 */
public class CallDetailRecordTest {

    @Test
    public void shouldSetCallDetailStatus() {
        CallDetailRecord callDetailRecord = new CallDetailRecord();
        callDetailRecord.setField("callStatus", "answered", null);
        assertEquals("answered", callDetailRecord.getCallStatus());
    }

    @Test
    public void shouldUseCallDetailStatusMapping() {
        CallDetailRecord callDetailRecord = new CallDetailRecord();
        Map mapping = setUpCallStatusMapping();

        callDetailRecord.setField("callStatus", "12", mapping);
        assertEquals("Error", callDetailRecord.getCallStatus());

        callDetailRecord.setField("callStatus", "1", mapping);
        assertEquals("Answered", callDetailRecord.getCallStatus());

        callDetailRecord.setField("callStatus", "2", mapping);
        assertEquals("2", callDetailRecord.getCallStatus());

    }

    @Test
    public void shouldTruncateLongValue() {
        CallDetailRecord callDetailRecord = new CallDetailRecord();
        StringBuilder sb = new StringBuilder();
        for (int i = 0 ; i<50 ; i++) {
            sb.append("0123456789");
        }
        String value = sb.toString();
        callDetailRecord.setField( "from", value, null);
        assertEquals(callDetailRecord.getFrom().length(), 255);
    }

    @Test
    public void shouldSetCallDetail() {
        CallDetailRecord callDetailRecord = new CallDetailRecord();
        callDetailRecord.setField( "to", "+12065551212", null);
        assertEquals("+12065551212", callDetailRecord.getTo());
    }

    @Test
    public void shouldSetCallDetailAsProviderExtraData() {
        CallDetailRecord callDetailRecord = new CallDetailRecord();
        Map mapping = setUpCallStatusMapping();

        callDetailRecord.setField( "provider-specific-stuff", "specific-value", mapping);
        assertTrue(callDetailRecord.getProviderExtraData().containsKey("provider-specific-stuff"));
        assertEquals("specific-value", callDetailRecord.getProviderExtraData().get("provider-specific-stuff"));
    }

    private Map setUpCallStatusMapping() {
        Map mapping = new HashMap<String, String>();

        mapping.put("12", "Error");
        mapping.put("1", "Answered");

        return mapping;
    }
}
