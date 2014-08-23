package org.motechproject.ivr.domain;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Config Unit Tests
 */
public class ConfigTest {

    @Test
    public void shouldIgnoreFields() {

        Config config = new Config(null, new ArrayList<>(Arrays.asList("foo")), null, null, null);
        assertTrue(config.shouldIgnoreField("foo"));
        assertFalse(config.shouldIgnoreField("bar"));
    }

    @Test
    public void verifyMappedStatusFields() {
        Map<String, String> statusMap = new HashMap<>();
        statusMap.put("foo", "bar");

        Config config = new Config(null, null, statusMap, null, null);
        assertEquals("bar", config.mapStatusField("foo"));
        assertEquals("zee", config.mapStatusField("zee"));
    }
}
