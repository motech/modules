package org.motechproject.commcare.tasks.builder;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.generic.EscapeTool;
import org.motechproject.commcare.config.Config;
import org.motechproject.commcare.service.CommcareConfigService;
import org.motechproject.commcare.service.CommcareSchemaService;
import org.motechproject.commcare.util.NameTrimmer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.annotation.Resource;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The <code>CommcareDataProviderBuilder</code> class is responsible for building
 * JSON file, containing information about available CommCare forms, fixtures, cases and
 * users. The constructed JSON is used in the {@link org.motechproject.commcare.CommcareDataProvider}
 */
@Component
public class CommcareDataProviderBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommcareDataProviderBuilder.class);

    private static final String COMMCARE_TASK_DATA_PROVIDER = "/velocity.templates/task-data-provider.vm";

    private VelocityEngine velocityEngine;
    private CommcareSchemaService schemaService;
    private CommcareConfigService configService;

    /**
     * Builds the body of the data provider into JSON form, using all available Commcare configs.
     * @return the body of the provider, or null if there are no configuration available
     */
    public String generateDataProvider() {
        Map<String, Object> model = new HashMap<>();
        EscapeTool escapeTool = new EscapeTool();
        NameTrimmer trimmer = new NameTrimmer();

        List<ConfigurationData> configurations = new ArrayList<>();
        for (Config config : configService.getConfigs().getConfigs()) {
            configurations.add(new ConfigurationData(config.getName(),
                    schemaService.getFormsWithApplicationName(config.getName()),
                    schemaService.getCaseTypesWithApplicationName(config.getName())));
        }

        if (configurations.isEmpty()) {
            // return null in case of no configurations - the provider won't get registered
            return null;
        }

        model.put("configurations", configurations);
        model.put("esc", escapeTool);
        model.put("trimmer", trimmer);
        model.put("DisplayNameHelper", DisplayNameHelper.class);
        StringWriter writer = new StringWriter();

        VelocityEngineUtils.mergeTemplate(velocityEngine, COMMCARE_TASK_DATA_PROVIDER, model, writer);

        String providerJson = writer.toString();

        LOGGER.trace("Generated the following tasks data provider: {}", providerJson);

        return providerJson;
    }

    @Resource(name = "commcareVelocityEngine")
    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    @Autowired
    public void setSchemaService(CommcareSchemaService schemaService) {
        this.schemaService = schemaService;
    }

    @Autowired
    public void setConfigService(CommcareConfigService configService) {
        this.configService = configService;
    }

}

