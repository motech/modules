package org.motechproject.sms.service;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
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

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * See {@link org.motechproject.sms.service.TemplateService}
 */
@Service("templateService")
public class TemplateServiceImpl implements TemplateService {

    private static final String SMS_TEMPLATE_FILE_NAME = "sms-templates.json";
    private static final String SMS_TEMPLATE_FILE_PATH = "/" + ConfigurationConstants.RAW_DIR + "/" +
        SMS_TEMPLATE_FILE_NAME;
    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateServiceImpl.class);
    private SettingsFacade settingsFacade;
    private Map<String, Template> templates = new HashMap<>();

    private synchronized void loadTemplates() {
        List<Template> templateList = null;
        try (InputStream is = settingsFacade.getRawConfig(SMS_TEMPLATE_FILE_NAME)) {
            String jsonText = IOUtils.toString(is);
            Gson gson = new Gson();
            templateList = gson.fromJson(jsonText, new TypeToken<List<Template>>() { } .getType());
        } catch (Exception e) {
            throw new JsonIOException("Malformed " + SMS_TEMPLATE_FILE_NAME + " file? " + e.toString(), e);
        }

        templates = new HashMap<>();
        for (Template template : templateList) {
            template.readDefaults(this.settingsFacade);
            templates.put(template.getName(), template);
        }
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


    public Template getTemplate(String name) {
        if (templates.containsKey(name)) {
            return templates.get(name);
        }
        String message = String.format("Unknown template: '%s'.");
        LOGGER.error(message);
        throw new IllegalArgumentException(message);
    }

    public Map<String, TemplateForWeb> allTemplatesForWeb() {
        Map<String, TemplateForWeb> ret = new HashMap<>();
        for (Map.Entry<String, Template> entry : templates.entrySet()) {
            ret.put(entry.getKey(), new TemplateForWeb(entry.getValue()));
        }
        return ret;
    }
}
