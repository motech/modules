package org.motechproject.openmrs.service.impl;

import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.domain.Form;
import org.motechproject.openmrs.exception.OpenMRSException;
import org.motechproject.openmrs.resource.FormResource;
import org.motechproject.openmrs.service.OpenMRSConfigService;
import org.motechproject.openmrs.service.OpenMRSFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service("formService")
public class OpenMRSFormServiceImpl implements OpenMRSFormService {

    @Autowired
    private final OpenMRSConfigService configService;
    private final FormResource formResource;

    @Autowired
    public OpenMRSFormServiceImpl(OpenMRSConfigService configService, FormResource formResource) {
        this.configService = configService;
        this.formResource = formResource;
    }

    @Override
    public Form getFormByUuid(String configName, String uuid) {
        try {
            Config config = configService.getConfigByName(configName);
            return formResource.getFormByUuid(config, uuid);
        } catch (HttpClientErrorException e) {
            throw new OpenMRSException(String.format("Could not get Form for uuid %s. %s %s", uuid, e.getMessage(), e.getResponseBodyAsString()), e);
        }
    }
}
