package org.motechproject.ivr.it;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.ivr.domain.Config;
import org.motechproject.ivr.domain.HttpMethod;
import org.motechproject.ivr.service.ConfigService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Verify ConfigService present & functional.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class ConfigServiceBundleIT extends BasePaxIT {

    @Inject
    private ConfigService configService;

    private List<Config> backupConfigs;

    @Before
    public void backupConfigs() {
        getLogger().info("backupConfigs");
        backupConfigs = configService.allConfigs();
    }

    @After
    public void restoreConfigs() {
        getLogger().info("restoreConfigs");
        configService.updateConfigs(backupConfigs);
    }

    @Test
    public void verifyServiceFunctional() {
        Config myConfig = new Config("MyConfig", false, null, null, null, null, null, HttpMethod.GET, "http://foo.com/bar", false, null);
        configService.updateConfigs(Arrays.asList(myConfig));

        Config config = configService.getConfig("MyConfig");
        assertEquals(config, myConfig);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotFindAbsentConfig() {
        configService.updateConfigs(Arrays.asList(new Config("foo", false, null, null, null, null, null, null, null, false, null)));
        configService.getConfig("bar");
    }
}
