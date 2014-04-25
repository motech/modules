package org.motechproject.server.alerts.osgi;

import org.apache.commons.beanutils.PropertyUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.motechproject.server.alerts.contract.AlertService;
import org.motechproject.server.alerts.contract.AlertsDataService;
import org.motechproject.server.alerts.domain.Alert;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class AlertsBaseIT extends BasePaxIT {

    @Inject
    protected AlertsDataService alertsDataService;

    @Inject
    protected AlertService alertService;

    @Before
    public void setUp() throws Exception {
        alertsDataService.deleteAll();
    }

    @After
    public void tearDown() throws Exception {
        alertsDataService.deleteAll();
    }

    protected void assertResults(List<Alert> actual, String property, Object... expecteds) throws Exception {
        assertEquals(expecteds.length, actual.size());
        boolean found = false;

        for (Object expected : expecteds) {
            for (Alert alert : actual) {
                Object value = PropertyUtils.getProperty(alert, property);
                if (value.equals(expected)) {
                    found = true;
                    break;
                }
            }
        }

        assertTrue(found);
    }

}
