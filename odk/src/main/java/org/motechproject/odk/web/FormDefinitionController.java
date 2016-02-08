package org.motechproject.odk.web;


import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.service.FormDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

/**
 * Controller that maps to /formDefinitions. Provides an API endpoint to access form definition records.
 */
@Controller
@RequestMapping("/formDefinitions")
public class FormDefinitionController {

    @Autowired
    private FormDefinitionService formDefinitionService;

    /**
     * Finds all form definitions by configuration name.
     * @param configName The name of the configuration.
     * @return A list of {@link FormDefinition}
     */
    @RequestMapping(value = "/{configName}", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<FormDefinition> getFormDefsByConfigName(@PathVariable("configName") String configName) {
        return formDefinitionService.findAllByConfigName(configName);
    }

    /**
     * Gets a particular form uniquely identified by the ID.
     * @param id The ID of the form.
     * @return {@link FormDefinition}
     */
    @RequestMapping(value = "/formdefinition/{id}", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public FormDefinition getFormDefById(@PathVariable("id") long id) {
        return formDefinitionService.findById(id);
    }

}
