package org.motechproject.sms.service;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.motechproject.commons.api.MotechException;
import org.motechproject.config.core.constants.ConfigurationConstants;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.server.config.SettingsFacade;
import org.motechproject.sms.templates.Template;
import org.motechproject.sms.templates.TemplateForWeb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * See {@link org.motechproject.sms.service.TemplateService}
 */
@Service("templateService")
public class TemplateServiceImpl implements TemplateService {

    private static final String SMS_TEMPLATE_CUSTOM_FILE_NAME = "sms-templates-custom.json";
    private static final String SMS_TEMPLATE_FILE_NAME = "sms-templates.json";
    private static final String SMS_TEMPLATE_FILE_PATH = "/" + ConfigurationConstants.RAW_DIR + "/" +
        SMS_TEMPLATE_FILE_NAME;
    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateServiceImpl.class);
    private SettingsFacade settingsFacade;
    private Map<String, Template> templates = new HashMap<>();

    @Override
    public Template getTemplate(String name) {
        if (templates.containsKey(name)) {
            return templates.get(name);
        }
        throw new IllegalArgumentException(String.format("Unknown template: '%s'.", name));
    }

    @Override
    public Map<String, TemplateForWeb> allTemplatesForWeb() {
        Map<String, TemplateForWeb> ret = new HashMap<>();
        for (Map.Entry<String, Template> entry : templates.entrySet()) {
            ret.put(entry.getKey(), new TemplateForWeb(entry.getValue()));
        }
        return ret;
    }

    @Override
    public void importTemplates(List<Template> templateList) {
        for (Template template : templateList) {
            importTemplate(template);
        }

        Gson gson = new Gson();
        String jsonText = gson.toJson(templateList, new TypeToken<List<Template>>() { } .getType());
        settingsFacade.saveRawConfig(SMS_TEMPLATE_CUSTOM_FILE_NAME, jsonText);
    }

    @Override
    public void importTemplate(Template template) {
        template.readDefaults(this.settingsFacade);
        templates.put(template.getName(), template);
    }

    @Autowired
    public TemplateServiceImpl(@Qualifier("smsSettings") SettingsFacade settingsFacade) {
        this.settingsFacade = settingsFacade;
        loadTemplates();
    }

    @MotechListener(subjects = { ConfigurationConstants.FILE_CHANGED_EVENT_SUBJECT })
    public void handleFileChanged(MotechEvent event) {
        String filePath = (String) event.getParameters().get(ConfigurationConstants.FILE_PATH);
        if (!StringUtils.isBlank(filePath) && filePath.endsWith(SMS_TEMPLATE_FILE_PATH)) {
            LOGGER.info("{} has changed, reloading templates.", SMS_TEMPLATE_FILE_NAME);
            loadTemplates();
        }
    }

    private synchronized void loadTemplates() {
        templates = new HashMap<>();
        load(SMS_TEMPLATE_FILE_NAME);
        load(SMS_TEMPLATE_CUSTOM_FILE_NAME);
    }

    private void load(String fileName) {
        List<Template> templateList = new ArrayList<>();

        try (InputStream is = settingsFacade.getRawConfig(fileName)) {
            String jsonText = IOUtils.toString(is);
            Gson gson = new Gson();
            if (StringUtils.isNotBlank(jsonText)) {
                 templateList = gson.fromJson(jsonText, new TypeToken<List<Template>>() {}.getType());
            }
        } catch (JsonParseException e) {
            throw new MotechException("File " + fileName + " is malformed", e);
        } catch (IOException e) {
            throw new MotechException("Error loading file " + fileName, e);
        }

        for (Template template : templateList) {
            template.readDefaults(this.settingsFacade);
            templates.put(template.getName(), template);
        }
    }
}
