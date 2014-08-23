package org.motechproject.ivr.it;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.motechproject.ivr.domain.Config;
import org.motechproject.ivr.domain.HttpMethod;
import org.motechproject.ivr.repository.ConfigDataService;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;
import javax.jdo.JDOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Verify ConfigDataService present & functional.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class ConfigDataServiceIT extends BasePaxIT {

    @Inject
    private ConfigDataService configDataService;

    @Before
    public void cleanupDatabase() {
        configDataService.deleteAll();
    }

    @Test
    public void verifyServiceFunctional() {
        getLogger().info("verifyServiceFunctional");

        Config myConfig = new Config("MyConfig", null, null, HttpMethod.GET, "http://foo.com/bar");
        configDataService.create(myConfig);

        Config config = configDataService.findByName("MyConfig");
        assertEquals(config, myConfig);
    }

    @Test(expected = JDOException.class)
    public void shouldNotCreateDuplicateConfigs() {
        getLogger().info("shouldNotCreateDuplicateConfigs");

        Config myConfig = new Config("MyConfig", null, null, HttpMethod.GET, "http://foo.com/bar");
        configDataService.create(myConfig);

        Config myIdenticalConfig = new Config("MyConfig", null, null, HttpMethod.GET, "http://foo.com/bar");
        configDataService.create(myIdenticalConfig);
    }

    public void shouldNotFindAbsentConfig() {
        getLogger().info("shouldNotFindAbsentConfig");

        Config myConfig = new Config("foo", null, null, HttpMethod.GET, "http://foo.com/bar");
        configDataService.create(myConfig);

        Config configFromDatabase = configDataService.findByName("bar");
        assertEquals(null, configFromDatabase);
    }

    @Test
    public void shouldStoreLongOutgoingCallUriTemplates() {
        getLogger().info("shouldStoreLongOutgoingCallUriTemplates");

        Map<String, String> outgoingCallUriParams = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0 ; i<100 ; i++) {
            sb.append("0123456789");
        }
        String longURI = sb.toString();

        Config myConfig = new Config("MyConfig", null, null, HttpMethod.GET, longURI);
        configDataService.create(myConfig);

        Config configFromDatabase = configDataService.findByName("MyConfig");
        assertEquals(configFromDatabase.getOutgoingCallUriTemplate(), longURI);
    }
}
