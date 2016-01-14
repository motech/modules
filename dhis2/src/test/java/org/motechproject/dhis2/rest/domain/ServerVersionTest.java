package org.motechproject.dhis2.rest.domain;

import org.junit.Assert;
import org.junit.Test;

public class ServerVersionTest {

    @Test
    public void shouldProperlyCompareVersions() {
        ServerVersion version = new ServerVersion(ServerVersion.V2_19);


        Assert.assertTrue(version.isAfter(ServerVersion.V2_18));
        Assert.assertTrue(version.isAfter("2.15"));
        Assert.assertTrue(version.isAfter("2.1"));
        Assert.assertTrue(version.isAfter("2.0"));
        Assert.assertTrue(version.isAfter("1.0"));
        Assert.assertTrue(version.isAfter("0.12"));
        Assert.assertFalse(version.isAfter(ServerVersion.V2_21));

        Assert.assertTrue(version.isSameOrAfter(ServerVersion.V2_19));
        Assert.assertTrue(version.isSameOrAfter(ServerVersion.V2_18));
        Assert.assertTrue(version.isSameOrAfter("2.15"));
        Assert.assertTrue(version.isSameOrAfter("2.1"));
        Assert.assertTrue(version.isSameOrAfter("2.0"));
        Assert.assertTrue(version.isSameOrAfter("1.0"));
        Assert.assertTrue(version.isSameOrAfter("0.12"));
        Assert.assertFalse(version.isSameOrAfter(ServerVersion.V2_21));

        Assert.assertTrue(version.isSame(ServerVersion.V2_19));
        Assert.assertFalse(version.isSame(ServerVersion.V2_18));

        Assert.assertTrue(version.isBefore(ServerVersion.V2_21));
        Assert.assertTrue(version.isBefore("2.22"));
        Assert.assertTrue(version.isBefore("2.30"));
        Assert.assertTrue(version.isBefore("3.0"));
        Assert.assertFalse(version.isBefore("2.1"));
        Assert.assertFalse(version.isBefore("2.17"));

        Assert.assertTrue(version.isSameOrBefore(ServerVersion.V2_19));
        Assert.assertTrue(version.isSameOrBefore(ServerVersion.V2_21));
        Assert.assertTrue(version.isSameOrBefore("2.22"));
        Assert.assertTrue(version.isSameOrBefore("2.30"));
        Assert.assertTrue(version.isSameOrBefore("3.0"));
        Assert.assertFalse(version.isSameOrBefore("2.1"));
        Assert.assertFalse(version.isSameOrBefore("2.17"));

    }
}
