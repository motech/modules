package org.motechproject.sms.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.motechproject.sms.service.TemplateService;
import org.motechproject.sms.templates.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * This class parses Json into Template objects and invokes suitable methods from <code>TemplateService</code>.
 *
 * @see org.motechproject.sms.service.TemplateService
 */
@Component
public class TemplateJsonParser {

    private TemplateService templateService;

    public void importTemplates(String jsonText) {
        Gson gson = new Gson();
        List<Template> templateList = gson.fromJson(jsonText, new TypeToken<List<Template>>() { } .getType());
        templateService.importTemplates(templateList);
    }

    public void importTemplate(String jsonText) {
        Gson gson = new Gson();
        Template template = gson.fromJson(jsonText, new TypeToken<Template>() { } .getType());
        templateService.importTemplate(template);
    }

    @Autowired
    public void setTemplateService(TemplateService templateService) {
        this.templateService = templateService;
    }
}
