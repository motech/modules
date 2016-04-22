package org.motechproject.openmrs19.tasks.builder;

import org.apache.velocity.app.VelocityEngine;
import org.motechproject.openmrs19.config.Config;
import org.motechproject.openmrs19.service.OpenMRSConfigService;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.annotation.Resource;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OpenMRSTaskDataProviderBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenMRSTaskDataProviderBuilder.class);
    private static final String OPENMRS_TASK_DATA_PROVIDER = "/velocity.templates/task-data-provider.vm";

    private VelocityEngine velocityEngine;
    private OpenMRSConfigService openMRSConfigService;

    public String generateDataProvider() {
        Map<String, Object> model = new HashMap<>();
        List<Config> configurations = openMRSConfigService.getConfigs();

        if (configurations.isEmpty()) {
            // return null in case of no configurations - the provider won't get registered
            return null;
        }

        model.put("configurations", configurations);

        StringWriter writer = new StringWriter();
        VelocityEngineUtils.mergeTemplate(velocityEngine, OPENMRS_TASK_DATA_PROVIDER, "UTF-8", model, writer);
        String providerJson = writer.toString();

        LOGGER.trace("Generated the following tasks data provider: {}", providerJson);

        return providerJson;
    }

    @Resource(name = "openMRSVelocityEngine")
    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    @Autowired
    public void setOpenMRSConfigService(OpenMRSConfigService openMRSConfigService) {
        this.openMRSConfigService = openMRSConfigService;
    }

}
