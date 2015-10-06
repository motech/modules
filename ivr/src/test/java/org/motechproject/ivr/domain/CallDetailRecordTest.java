package org.motechproject.ivr.domain;

import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * CallDetailRecord Unit Tests
 */
public class CallDetailRecordTest {

    private static final String MESSAGE_PERCENT_LISTENED = "messagePercentListened";
    private static final String CALL_DURATION = "callDuration";

    @Test
    public void shouldSetCallDetailStatus() {
        CallDetailRecord callDetailRecord = new CallDetailRecord();
        callDetailRecord.setField("callStatus", "answered", null);
        assertEquals("answered", callDetailRecord.getCallStatus());
    }

    @Test
    public void shouldUseCallDetailStatusMapping() {
        CallDetailRecord callDetailRecord = new CallDetailRecord();
        Map<String, String> mapping = setUpCallStatusMapping();

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
        Map<String, String> mapping = setUpCallStatusMapping();

        callDetailRecord.setField( "provider-specific-stuff", "specific-value", mapping);
        assertTrue(callDetailRecord.getProviderExtraData().containsKey("provider-specific-stuff"));
        assertEquals("specific-value", callDetailRecord.getProviderExtraData().get("provider-specific-stuff"));
    }

    @Test
    public void shouldHandleDecimalsProperly() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Map<String, String> mapping = setUpCallStatusMapping();
        CallDetailRecord cdr = new CallDetailRecord();

        for (String field : Arrays.asList(MESSAGE_PERCENT_LISTENED, CALL_DURATION)) {
            cdr.setField(field, null, mapping);
            assertField(cdr, field, null);

            cdr.setField(field, "", mapping);
            assertField(cdr, field, "");

            cdr.setField(field, "nonNumber", mapping);
            assertField(cdr, field, "nonNumber");

            cdr.setField(field, "4.247342", mapping);
            assertField(cdr, field, "4.25");

            cdr.setField(field, "4.00", mapping);
            assertField(cdr, field, "4");

            cdr.setField(field, "4.10", mapping);
            assertField(cdr, field, "4.1");

            cdr.setField(field, "100", mapping);
            assertField(cdr, field, "100");
        }
    }

    private Map<String, String> setUpCallStatusMapping() {
        Map<String, String> mapping = new HashMap<>();

        mapping.put("12", "Error");
        mapping.put("1", "Answered");

        return mapping;
    }

    private void assertField(CallDetailRecord cdr, String field, String expectedValue)
            throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        assertEquals("Field has wrong value: " + field, expectedValue, PropertyUtils.getProperty(cdr, field));
    }
}
