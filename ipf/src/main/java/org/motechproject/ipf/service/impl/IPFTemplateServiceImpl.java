package org.motechproject.ipf.service.impl;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.motechproject.ipf.service.IPFTemplateService;
import org.motechproject.ipf.util.Constants;
import org.motechproject.server.config.SettingsFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

@Service("ipfTemplateService")
public class IPFTemplateServiceImpl implements IPFTemplateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IPFTemplateServiceImpl.class);

    private List<String> loadedTemplates;

    @Autowired
    @Qualifier("ipfSettings")
    private SettingsFacade settingsFacade;

    @PostConstruct
    public void init() {
        loadedTemplates = new ArrayList<>();
        Properties properties = settingsFacade.getProperties(Constants.IPF_PROPERTIES_FILE);
        if (properties == null || StringUtils.isEmpty(properties.getProperty(Constants.IPF_TEMPLATE_KEY))) {
            LOGGER.debug("Cannot find {} file or {} is blank.", Constants.IPF_PROPERTIES_FILE, Constants.IPF_TEMPLATE_KEY);
            return;
        }

        String templatesLocation = properties.getProperty(Constants.IPF_TEMPLATE_KEY);

        for (File templateFile : getAllTemplateFiles(templatesLocation)) {
            LOGGER.debug("Parsing {} template file", templateFile.getName());
            //TODO parsing ?
            try(InputStream is = new FileInputStream(templateFile)) {
                settingsFacade.saveRawConfig(templateFile.getName(), IOUtils.toString(is));
                loadedTemplates.add(templateFile.getName());
            } catch (IOException e) {
                // TODO ? throw exception or continue ?
                LOGGER.error("Cannot read {} template file", templateFile.getName(), e);
            }
        }
    }

    @Override
    public List<String> getAllTemplateNames() {
        return loadedTemplates;
    }

    private File[] getAllTemplateFiles(String templatePath) {
        File templateDirectory = FileUtils.getFile(templatePath);
        if (!templateDirectory.exists()) {
            LOGGER.debug("Location {} doesn't exist. Cannot load IPF templates.", templatePath);
            return new File[0];
        }

        if(!templateDirectory.isDirectory()) {
            throw new IllegalArgumentException(templatePath + " is not a directory.");
        }

        final Pattern tempplateFileNamePattern = Pattern.compile(Constants.IPF_TEMPLATE_FILE_NAME_PATTERN);
        return templateDirectory.listFiles(new FileFilter(){
            @Override
            public boolean accept(File file) {
                return tempplateFileNamePattern.matcher(file.getName()).matches();
            }
        });
    }
}
