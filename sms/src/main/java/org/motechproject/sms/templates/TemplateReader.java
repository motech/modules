package org.motechproject.sms.templates;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import org.motechproject.server.config.SettingsFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Reads template info from sms-templates.json into a list of Template objects
 */
@Component
public class TemplateReader {

    public static final String FILE_NAME = "sms-templates.json";
    private String templateFileName;
    private SettingsFacade settingsFacade;

    @Autowired
    public TemplateReader(@Qualifier("smsSettings") SettingsFacade settingsFacade) {
        this.settingsFacade = settingsFacade;
        this.templateFileName = FILE_NAME;
    }

    //todo: cache that & use the soon coming configs feature to get notification & refresh if the data changes
    public Templates getTemplates() {
        List<Template> templates = null;
        Type type = new TypeToken<List<Template>>() { } .getType();
        InputStream is = settingsFacade.getRawConfig(templateFileName);
        String errorMessage = null;
        try {
            String jsonText = IOUtils.toString(is);
            Gson gson = new Gson();
            templates = gson.fromJson(jsonText, type);
        } catch (Exception e) {
            //todo: what do we do with these? (might be coming from malformed .json config file)
            errorMessage = "Might you have a malformed " + FILE_NAME + " file? " + e.toString();
        }

        if (templates == null) {
            if (errorMessage == null) {
                errorMessage = "Unable to read templates.";
            }
            throw new JsonIOException(errorMessage);
        }
        return new Templates(settingsFacade, templates);
    }
}
