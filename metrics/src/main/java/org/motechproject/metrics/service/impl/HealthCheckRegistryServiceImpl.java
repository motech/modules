package org.motechproject.metrics.service.impl;

import com.codahale.metrics.health.HealthCheckRegistry;
import org.motechproject.metrics.api.HealthCheck;
import org.motechproject.metrics.exception.HealthCheckException;
import org.motechproject.metrics.service.HealthCheckRegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.SortedSet;

/**
 * Represents a registry that allows healthchecks to be added and removed, and gives a list of the names of all
 * healthchecks currently registered.
 */
@Service("healthCheckRegistryService")
public class HealthCheckRegistryServiceImpl implements HealthCheckRegistryService {
    private HealthCheckRegistry healthCheckRegistry;

    @Autowired
    public HealthCheckRegistryServiceImpl(HealthCheckRegistry healthCheckRegistry) {
        this.healthCheckRegistry = healthCheckRegistry;
    }

    /**
     * Registers a healthcheck with the metrics module, allowing the healthcheck to be polled using the supported mechanisms.
     *
     * @param name the name of the healtcheck
     * @param healthCheck an implementation of the healthcheck interface
     */
    @Override
    public void register(String name, HealthCheck healthCheck) {
        healthCheckRegistry.register(name, new com.codahale.metrics.health.HealthCheck() {
            @Override
            protected Result check() throws HealthCheckException {
                return convertToCodaHaleResult(healthCheck.check());
            }
        });
    }

    /**
     * Unregisters a healthcheck by name.
     *
     * @param name the name of the healthcheck
     */
    @Override
    public void unregister(String name) {
        healthCheckRegistry.unregister(name);
    }

    /**
     * Returns a set of the name of all healthchecks in sorted order.
     *
     * @return the sorted set of names.
     */
    @Override
    public SortedSet<String> getNames() {
        return healthCheckRegistry.getNames();
    }

    private com.codahale.metrics.health.HealthCheck.Result convertToCodaHaleResult(HealthCheck.Result result) {
        com.codahale.metrics.health.HealthCheck.Result codaResult;
        String message = result.getMessage();
        if (result.isHealthy()) {
            if (message != null) {
                codaResult = com.codahale.metrics.health.HealthCheck.Result.healthy(message);
            } else {
                codaResult = com.codahale.metrics.health.HealthCheck.Result.healthy();
            }
        } else {
            Throwable error = result.getError();
            if (error != null) {
                codaResult = com.codahale.metrics.health.HealthCheck.Result.unhealthy(error);
            } else {
                codaResult = com.codahale.metrics.health.HealthCheck.Result.unhealthy(message);
            }
        }
        return codaResult;
    }
}
