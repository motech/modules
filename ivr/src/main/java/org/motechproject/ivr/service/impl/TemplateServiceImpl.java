package org.motechproject.ivr.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.motechproject.config.core.constants.ConfigurationConstants;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.ivr.domain.Template;
import org.motechproject.ivr.exception.TemplateNotFoundException;
import org.motechproject.ivr.service.TemplateService;
import org.motechproject.server.config.SettingsFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * See {@link org.motechproject.ivr.service.TemplateService}
 */
@Service("templateService")
public class TemplateServiceImpl implements TemplateService {
    private static final String TEMPLATE_FILE_NAME = "ivr-templates.json";
    private static final String TEMPLATE_FILE_PATH = "/org.motechproject.ivr/raw/" + TEMPLATE_FILE_NAME;
    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateServiceImpl.class);
    private SettingsFacade settingsFacade;
    private Map<String, Template> templates = new HashMap<>();

    private synchronized void loadTemplates() {
        List<Template> templateList = null;
        try (InputStream is = settingsFacade.getRawConfig(TEMPLATE_FILE_NAME)) {
            String jsonText = IOUtils.toString(is);
            LOGGER.debug("Reloading {}", TEMPLATE_FILE_NAME);
            Gson gson = new Gson();
            templateList = gson.fromJson(jsonText, new TypeToken<List<Template>>() { } .getType());
        } catch (Exception e) {
            String message = String.format("There seems to be a problem with the json text in %s: %s",
                    TEMPLATE_FILE_NAME, e.getMessage());
            throw new JsonIOException(message, e);
        }

        templates = new HashMap<>();
        for (Template template : templateList) {
            templates.put(template.getName(), template);
        }
    }

    @Autowired
    public TemplateServiceImpl(@Qualifier("ivrSettings") SettingsFacade settingsFacade) {
        this.settingsFacade = settingsFacade;
        loadTemplates();
    }

    @MotechListener(subjects = { ConfigurationConstants.FILE_CHANGED_EVENT_SUBJECT })
    public void handleFileChanged(MotechEvent event) {
        String filePath = (String) event.getParameters().get(ConfigurationConstants.FILE_PATH);
        if (!StringUtils.isBlank(filePath) && filePath.endsWith(TEMPLATE_FILE_PATH)) {
            LOGGER.info("{} has changed, reloading templates.", TEMPLATE_FILE_NAME);
            loadTemplates();
        }
    }

    public Template getTemplate(String name) {
        if (templates.containsKey(name)) {
            return templates.get(name);
        }
        throw new TemplateNotFoundException(String.format("Unknown template: '%s'.", name));
    }

    public List<Template> allTemplates() {
        return new ArrayList<Template>(templates.values());
    }

    public boolean hasTemplate(String name) {
        return templates.containsKey(name);
    }

    public void updateTemplates(List<Template> templates) {
        Gson gson = new Gson();
        String jsonText = gson.toJson(templates);
        ByteArrayResource resource = new ByteArrayResource(jsonText.getBytes());
        settingsFacade.saveRawConfig(TEMPLATE_FILE_NAME, resource);
        loadTemplates();
    }
}
