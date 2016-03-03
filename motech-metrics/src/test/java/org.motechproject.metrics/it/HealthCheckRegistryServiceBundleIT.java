package org.motechproject.metrics.it;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.metrics.api.HealthCheck;
import org.motechproject.metrics.exception.HealthCheckException;
import org.motechproject.metrics.service.HealthCheckRegistryService;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class HealthCheckRegistryServiceBundleIT {
    @Inject
    private HealthCheckRegistryService healthCheckRegistryService;

    @Test
    public void shouldRegisterHealthCheckRegistryServiceInstance() {
        assertNotNull(healthCheckRegistryService);
    }

    @Test
    public void shouldRegisterDefaultHealthCheck() {
        Set<String> registered = healthCheckRegistryService.getNames();

        assertNotNull(registered);

        System.out.println(registered);

        assertTrue(registered.size() == 1);
        assertTrue(registered.contains("threadDeadlockHealthCheck"));
    }

    @Test
    public void shouldRegisterAndUnregisterMetric() {
        healthCheckRegistryService.register("foo", new HealthCheck() {
            @Override
            public Result check() throws HealthCheckException {
                return null;
            }
        });

        Set<String> registered = healthCheckRegistryService.getNames();

        assertNotNull(registered);
        assertTrue(registered.size() == 2);
        assertTrue(registered.contains("foo"));

        healthCheckRegistryService.unregister("foo");

        Set<String> unregistered = healthCheckRegistryService.getNames();

        assertTrue(unregistered.size() == 1);
        assertFalse(unregistered.contains("foo"));
    }
}
