package org.motechproject.metrics.service;

import org.motechproject.metrics.api.HealthCheck;

import java.util.SortedSet;

/**
 * Represents a registry that allows healthchecks to be added and removed, and gives a list of the names of all
 * healthchecks currently registered.
 */
public interface HealthCheckRegistryService {
    /**
     * Registers a healthcheck with the metrics module, allowing the healthcheck to be polled using the supported mechanisms.
     *
     * @param name the name of the healtcheck
     * @param healthCheck an implementation of the healthcheck interface
     */
    void register(String name, HealthCheck healthCheck);

    /**
     * Unregisters a healthcheck by name.
     *
     * @param name the name of the healthcheck
     */
    void unregister(String name);

    /**
     * Returns a set of the name of all healthchecks in sorted order.
     *
     * @return the sorted set of names.
     */
    SortedSet<String> getNames();
}
