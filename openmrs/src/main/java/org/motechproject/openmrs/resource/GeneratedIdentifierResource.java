package org.motechproject.openmrs.resource;

import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.domain.GeneratedIdentifier;

/**
 * Interface for Identifier Generator Management
 */
public interface GeneratedIdentifierResource {

    /**
     * Gets latest generated identifier by Generator with given {@code sourceName}.
     * The given {@code config} will be used while performing this action.
     *
     * @param config the configuration to be used while performing this action
     * @param sourceName the source name of the Id Generator
     * @return latest generated identifier
     */
    GeneratedIdentifier getGeneratedIdentifier(Config config, String sourceName);

    /**
     * Sets latest identifier {@code identifier} for Generator with given {@code sourceName}.
     * Configuration with the given {@code configName} will be used while performing this action.
     *
     * @param config the configuration to be used while performing this action
     * @param sourceName the source name of the ID Generator
     * @param identifier identifier to set
     */
    void setLatestIdentifier(Config config, String sourceName, String identifier);
}
