package org.motechproject.commcare.util;

import org.joda.time.DateTime;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CommcareParamHelperTest {

    @Test
    public void shouldPrintDateTime() {
        final DateTime dt = new DateTime(2012, 9, 27, 10, 50, 11);
        assertEquals("2012-09-27T10:50:11", CommcareParamHelper.printDateTime(dt));
    }

    @Test
    public void shouldCalculateOffsetsFromPagingParams() {
        assertEquals(0, CommcareParamHelper.toOffset(null, null));
        assertEquals(0, CommcareParamHelper.toOffset(1000, null));
        assertEquals(100, CommcareParamHelper.toOffset(100, 2));
        assertEquals(0, CommcareParamHelper.toOffset(100, 1));
        assertEquals(75, CommcareParamHelper.toOffset(25, 4));
    }
}
