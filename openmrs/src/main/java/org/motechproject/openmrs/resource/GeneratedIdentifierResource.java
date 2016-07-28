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
}
