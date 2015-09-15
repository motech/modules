package org.motechproject.ivr.it;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.ivr.domain.Config;
import org.motechproject.ivr.domain.Configs;
import org.motechproject.ivr.domain.HttpMethod;
import org.motechproject.ivr.exception.ConfigNotFoundException;
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

    private Configs backupConfigs;

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
        Config myConfig = new Config("MyConfig", false, null, null, null, null, null, null, HttpMethod.GET, false, "http://foo.com/bar", false, null);
        Configs configs = new Configs();
        configs.setConfigList(Arrays.asList(myConfig));
        configs.setDefaultConfig("MyConfig");
        configService.updateConfigs(configs);

        Config config = configService.getConfig("MyConfig");
        assertEquals(config, myConfig);
    }

    @Test(expected = ConfigNotFoundException.class)
    public void shouldNotFindAbsentConfig() {
        List<Config> configList = Arrays.asList(new Config("foo", false, null, null, null, null, null, null, null, false, null, false, null));
        Configs configs = new Configs();
        configs.setConfigList(configList);
        configs.setDefaultConfig("foo");
        configService.updateConfigs(configs);
        configService.getConfig("bar");
    }

    @Test
    public void verifyDefaultConfig() {
        List<Config> configList = Arrays.asList(new Config("foo", false, null, null, null, null, null, null, null, false, null, false, null));
        Configs configs = new Configs();
        configs.setConfigList(configList);
        configs.setDefaultConfig("xyz");
        configService.updateConfigs(configs);
        String defaultConfig = configService.getDefaultConfig();
        assertEquals("xyz", defaultConfig);

    }
}
