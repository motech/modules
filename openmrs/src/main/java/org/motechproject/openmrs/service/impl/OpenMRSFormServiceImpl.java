package org.motechproject.openmrs.service.impl;

import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.domain.Form;
import org.motechproject.openmrs.resource.FormResource;
import org.motechproject.openmrs.service.OpenMRSConfigService;
import org.motechproject.openmrs.service.OpenMRSFormService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service("formService")
public class OpenMRSFormServiceImpl implements OpenMRSFormService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenMRSFormServiceImpl.class);

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
            return formResource.getFormById(config, uuid);
        } catch (HttpClientErrorException e) {
            LOGGER.error("Error while fetching form with UUID: " + uuid);
            return null;
        }
    }
}
