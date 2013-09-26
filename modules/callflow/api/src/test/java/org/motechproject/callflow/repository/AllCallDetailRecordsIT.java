package org.motechproject.callflow.repository;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.ivr.domain.CallDetail;
import org.motechproject.ivr.domain.CallDetailRecord;
import org.motechproject.ivr.domain.CallDirection;
import org.motechproject.ivr.domain.CallDisposition;
import org.motechproject.ivr.repository.AllCallDetailRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests functionality of AllCallDetailRecords.
 * Assumes the database is empty before tests
 * (if it's not, the shouldFindMaxCallDuration test may fail).
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:META-INF/motech/*.xml")
public class AllCallDetailRecordsIT {

    public static final String PHONE_NUMBER_1 = "99991234561";
    public static final String PHONE_NUMBER_2 = "99991234671";

    private static final int MAX_CALL_DURATION = 50;

    @Autowired
    AllCallDetailRecords allCallDetailRecords;

    @Before
    public void setUp() {
        allCallDetailRecords.add(getRecord(PHONE_NUMBER_1, MAX_CALL_DURATION - 10));
        allCallDetailRecords.add(getRecord(PHONE_NUMBER_2, MAX_CALL_DURATION));
    }

    private CallDetailRecord getRecord(String phoneNumber, int duration) {
        final CallDetailRecord log = new CallDetailRecord("1", phoneNumber);
        log.setAnswerDate(DateUtil.now().toDate());
        log.setStartDate(DateUtil.now());
        log.setEndDate(DateUtil.now());
        log.setDuration(duration);
        log.setDisposition(CallDisposition.UNKNOWN);
        log.setCallDirection(CallDirection.Inbound);
        return log;
    }

    /**
     * Tests that record added to database is found via search.
     * @throws Exception
     */
    @Test
    public void shouldSearchCalllogs() throws Exception {
        DateTime endTime = DateTime.now().plusDays(1);
        DateTime startTime = DateTime.now().minusDays(1);
        int maxDuration = MAX_CALL_DURATION;
        final List<CallDetailRecord> rowList = allCallDetailRecords.search(PHONE_NUMBER_1, startTime, endTime, null, null, null, null, 0, maxDuration,
                Arrays.asList(CallDisposition.UNKNOWN.name()), Arrays.asList(CallDirection.Inbound.name()), null, false);
        assertTrue(rowList.size() > 0);
    }

    @Test
    public void shouldSearchCallsWithSpecificDuration() throws Exception {
        final List<CallDetailRecord> rowList = allCallDetailRecords.search(null, DateTime.now().minusDays(1),
                DateTime.now().plusDays(1), null, null, null, null, null, null, null, null, null, false);
        assertTrue(rowList.size() > 0);
    }

    @Test
    public void shouldReturnBasedOnGivenSortByParamInDescendingOrder() throws Exception {
        List<CallDetailRecord> rowList = allCallDetailRecords.search("99991234*", DateTime.now().minusDays(1),
                DateTime.now().plusDays(1), null, null, null, null, null, null, null,null, "phoneNumber", true);
        assertEquals(rowList.get(0).getPhoneNumber(), PHONE_NUMBER_2);

    }

    @Test
    public void shouldReturnBasedOnGivenSortByParamInAscendingOrder() throws Exception {
        List<CallDetailRecord> rowList = allCallDetailRecords.search("99991234*", DateTime.now().minusDays(1),
                DateTime.now().plusDays(1), null, null, null, null, null, null, null, null, "phoneNumber", false);
        assertEquals(PHONE_NUMBER_1, rowList.get(0).getPhoneNumber());
    }

    @Test
    public void shouldReturnTheTotalNumberOfCallRecords() {
        long count = allCallDetailRecords.countRecords("99991234*", DateTime.now().minusDays(1),
                DateTime.now().plusDays(1), null, null, null, null, null, null, null, null);
        assertEquals(2, count);
    }

    @Test
    public void shouldFindMaxCallDuration() {
        assertEquals(MAX_CALL_DURATION, allCallDetailRecords.findMaxCallDuration());
    }

    @Test
    public void shouldUpdateExistingCallRecords() {
        CallDetailRecord cdr = new CallDetailRecord("callId", PHONE_NUMBER_1);
        allCallDetailRecords.addOrUpdate(cdr);

        CallDetailRecord fromDb = allCallDetailRecords.findByCallId("callId");
        assertNotNull(fromDb);
        assertEquals("callId", fromDb.getCallId());
        assertEquals(PHONE_NUMBER_1, fromDb.getPhoneNumber());

        cdr = new CallDetailRecord("callId", PHONE_NUMBER_2);
        allCallDetailRecords.addOrUpdate(cdr);

        fromDb = allCallDetailRecords.findByCallId("callId");
        assertNotNull(fromDb);
        assertEquals("callId", fromDb.getCallId());
        assertEquals(PHONE_NUMBER_2, fromDb.getPhoneNumber());
    }

    @After
    public void tearDown() {
        final List<CallDetailRecord> logs = allCallDetailRecords.search(PHONE_NUMBER_1, DateTime.now().minusDays(1), DateTime.now().plusDays(1),
                null, null, null, null, null, null, null,null, null, false);
        logs.addAll(allCallDetailRecords.search(PHONE_NUMBER_2, DateTime.now().minusDays(1), DateTime.now().plusDays(1),
                null, null, null, null, null, null, null, null, null, false));
        for (CallDetail log : logs) {
            allCallDetailRecords.remove((CallDetailRecord) log);
        }
    }

}
