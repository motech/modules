package org.motechproject.dhis2.rest.domain;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class ServerVersionTest {

    @Test
    public void shouldProperlyCompareVersions() {
        ServerVersion version = new ServerVersion(ServerVersion.V2_19);


        assertTrue(version.isAfter(ServerVersion.V2_18));
        assertTrue(version.isAfter("2.15"));
        assertTrue(version.isAfter("2.1"));
        assertTrue(version.isAfter("2.0"));
        assertTrue(version.isAfter("1.0"));
        assertTrue(version.isAfter("0.12"));
        assertFalse(version.isAfter(ServerVersion.V2_21));

        assertTrue(version.isSameOrAfter(ServerVersion.V2_19));
        assertTrue(version.isSameOrAfter(ServerVersion.V2_18));
        assertTrue(version.isSameOrAfter("2.15"));
        assertTrue(version.isSameOrAfter("2.1"));
        assertTrue(version.isSameOrAfter("2.0"));
        assertTrue(version.isSameOrAfter("1.0"));
        assertTrue(version.isSameOrAfter("0.12"));
        assertFalse(version.isSameOrAfter(ServerVersion.V2_21));

        assertTrue(version.isSame(ServerVersion.V2_19));
        assertFalse(version.isSame(ServerVersion.V2_18));

        assertTrue(version.isBefore(ServerVersion.V2_21));
        assertTrue(version.isBefore("2.22"));
        assertTrue(version.isBefore("2.30"));
        assertTrue(version.isBefore("3.0"));
        assertFalse(version.isBefore("2.1"));
        assertFalse(version.isBefore("2.17"));

        assertTrue(version.isSameOrBefore(ServerVersion.V2_19));
        assertTrue(version.isSameOrBefore(ServerVersion.V2_21));
        assertTrue(version.isSameOrBefore("2.22"));
        assertTrue(version.isSameOrBefore("2.30"));
        assertTrue(version.isSameOrBefore("3.0"));
        assertFalse(version.isSameOrBefore("2.1"));
        assertFalse(version.isSameOrBefore("2.17"));

    }
}
