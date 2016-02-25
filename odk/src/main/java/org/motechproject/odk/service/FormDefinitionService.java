package org.motechproject.odk.service;

import org.motechproject.odk.domain.FormDefinition;

import java.util.List;

/**
 * Service for CRUD operations on {@link FormDefinition}
 */
public interface FormDefinitionService {

    /**
     * Persists a new form definition
     * @param formDefinition {@link FormDefinition}
     */
    void create(FormDefinition formDefinition);

    /**
     * Deletes all form definition records.
     */
    void deleteAll();

    /**
     * Deletes all form definitions associated with a particular configuration.
     * @param configName The name of the configuration
     */
    void deleteFormDefinitionsByConfigurationName(String configName);

    /**
     * Returns all form definition records.
     * @return A list of {@link FormDefinition}
     */
    List<FormDefinition> findAll();

    /**
     * Finds the unique form definition with a particular title that is associated with a particular configuration.
     * @param configurationName The name of the configuration
     * @param title The title of the form.
     * @return {@link FormDefinition}
     */
    FormDefinition findByConfigurationNameAndTitle(String configurationName, String title);

    /**
     * Returns all form definitions associated with a configuration.
     * @param configName The name of the configuration
     * @return A list of {@link FormDefinition}
     */
    List<FormDefinition> findAllByConfigName(String configName);

    /**
     * Finds a form definition by its ID
     * @param id A long value which uniquely identifies the form definition
     * @return {@link FormDefinition}
     */
    FormDefinition findById(long id);
}
