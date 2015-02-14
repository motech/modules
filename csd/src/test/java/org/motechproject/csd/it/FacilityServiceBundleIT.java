package org.motechproject.csd.it;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.csd.domain.Facility;
import org.motechproject.csd.service.FacilityService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;

import static org.junit.Assert.assertNull;

/**
 * Verify ConfigService present & functional.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class FacilityServiceBundleIT extends BasePaxIT {

    @Inject
    private FacilityService service;


    @Test
    public void verifyServiceFunctional() {

        Facility facility = service.getFacility("123");
        assertNull(facility);
    }
}
