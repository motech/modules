package org.motechproject.odk.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.odk.domain.FormDefinition;

import java.util.List;


/**
 * Data service for {@link FormDefinition}
 */
public interface FormDefinitionDataService extends MotechDataService<FormDefinition> {


    /**
     * Finds a list of form definitions associated with a particular configuration.
     * @param configurationName The name of the configuration/
     * @return A list of {@link FormDefinition}
     */
    @Lookup
    List<FormDefinition> byConfigurationName(@LookupField(name = "configurationName") String configurationName);

    /**
     * Finds the form definition by configuration name and title.
     * @param configurationName The name of the configuration.
     * @param title The title of the form.
     * @return {@link FormDefinition}
     */
    @Lookup
    FormDefinition byConfigurationNameAndTitle(@LookupField(name = "configurationName") String configurationName, @LookupField(name = "title") String title);

}
