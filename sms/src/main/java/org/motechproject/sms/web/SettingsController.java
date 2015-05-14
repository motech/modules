package org.motechproject.sms.web;

import org.apache.commons.io.IOUtils;
import org.motechproject.sms.configs.Configs;
import org.motechproject.sms.json.TemplateJsonParser;
import org.motechproject.sms.service.ConfigService;
import org.motechproject.sms.service.TemplateService;
import org.motechproject.sms.templates.TemplateForWeb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

//todo: find a way to report useful information if encountering malformed templates?

/**
 * Sends templates to the UI, sends & received configs to/from the UI.
 */
@Controller
public class SettingsController {
    private TemplateService templateService;
    private ConfigService configService;
    private TemplateJsonParser templateJsonParser;

    @Autowired
    public SettingsController(@Qualifier("templateService") TemplateService templateService,
                              @Qualifier("configService") ConfigService configService,
                              TemplateJsonParser templateJsonParser) {
        this.templateService = templateService;
        this.configService = configService;
        this.templateJsonParser = templateJsonParser;
    }

    @RequestMapping(value = "/templates", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, TemplateForWeb> getTemplates() {
        return templateService.allTemplatesForWeb();
    }

    @RequestMapping(value = "/templates/import", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void importTemplates(@RequestParam(value = "jsonFile") MultipartFile jsonFile)
            throws IOException {
        StringWriter writer = new StringWriter();
        IOUtils.copy(jsonFile.getInputStream(), writer);

        templateJsonParser.importTemplates(writer.toString());
    }

    @RequestMapping(value = "/configs", method = RequestMethod.GET)
    @ResponseBody
    public Configs getConfigs() {
        return configService.getConfigs();
    }

    @RequestMapping(value = "/configs", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Configs setConfigs(@RequestBody Configs configs) {
        configService.updateConfigs(configs);
        return configService.getConfigs();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public String handleException(Exception e) throws IOException {
        return e.getMessage();
    }
}
