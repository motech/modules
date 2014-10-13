package org.motechproject.commcare.tasks.builder;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.generic.EscapeTool;
import org.motechproject.commcare.service.CommcareSchemaService;
import org.motechproject.commcare.util.NameTrimmer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.annotation.Resource;
import java.io.StringWriter;
import java.util.HashMap;
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

    public String generateDataProvider() {
        Map<String, Object> model = new HashMap<>();
        EscapeTool escapeTool = new EscapeTool();
        NameTrimmer trimmer = new NameTrimmer();
        model.put("formSchemaList", schemaService.getAllFormSchemas());
        model.put("caseTypesMap", schemaService.getAllCaseTypes());
        model.put("esc", escapeTool);
        model.put("trimmer", trimmer);

        StringWriter writer = new StringWriter();

        try {
            VelocityEngineUtils.mergeTemplate(velocityEngine, COMMCARE_TASK_DATA_PROVIDER, model, writer);
        } catch (Exception e) {
            LOGGER.error("An error occured while trying to merge velocity template " +
                    COMMCARE_TASK_DATA_PROVIDER + " with data.");
        }

        return writer.toString();
    }

    @Resource(name = "commcareVelocityEngine")
    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    @Autowired
    public void setSchemaService(CommcareSchemaService schemaService) {
        this.schemaService = schemaService;
    }

}
