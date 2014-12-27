package org.motechproject.ivr.domain;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * CallDetailRecord Unit Tests
 */
public class CallDetailRecordTest {

    @Test
    public void shouldSetCallDetailStatus() {
        CallDetailRecord callDetailRecord = new CallDetailRecord();
        callDetailRecord.setField("callStatus", "answered");
        assertEquals("answered", callDetailRecord.getCallStatus());
    }

    @Test
    public void shouldTruncateLongValue() {
        CallDetailRecord callDetailRecord = new CallDetailRecord();
        StringBuilder sb = new StringBuilder();
        for (int i = 0 ; i<50 ; i++) {
            sb.append("0123456789");
        }
        String value = sb.toString();
        callDetailRecord.setField( "from", value);
        assertEquals(callDetailRecord.getFrom().length(), 255);
    }

    @Test
    public void shouldSetCallDetail() {
        CallDetailRecord callDetailRecord = new CallDetailRecord();
        callDetailRecord.setField( "to", "+12065551212");
        assertEquals("+12065551212", callDetailRecord.getTo());
    }

    @Test
    public void shouldSetCallDetailAsProviderExtraData() {
        CallDetailRecord callDetailRecord = new CallDetailRecord();
        callDetailRecord.setField( "provider-specific-stuff", "specific-value");
        assertTrue(callDetailRecord.getProviderExtraData().containsKey("provider-specific-stuff"));
        assertEquals("specific-value", callDetailRecord.getProviderExtraData().get("provider-specific-stuff"));
    }
}
