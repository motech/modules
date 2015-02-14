package org.motechproject.csd.domain;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

/**
 * Facility Unit Tests
 */
public class FacilityTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(FacilityTest.class);

    @Test
    public void verifyMappedStatusFields() {
        Facility facility = new Facility("uuid", "primaryName");
        assertEquals("uuid", facility.getUuid());
        assertEquals("primaryName", facility.getPrimaryName());
    }
}
