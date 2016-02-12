package org.motechproject.ipf.service.impl;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.motechproject.ipf.domain.IPFTemplate;
import org.motechproject.ipf.service.IPFTemplateDataService;
import org.motechproject.ipf.service.IPFTemplateService;
import org.motechproject.ipf.util.Constants;
import org.motechproject.server.config.SettingsFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

@Service("ipfTemplateService")
public class IPFTemplateServiceImpl implements IPFTemplateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IPFTemplateServiceImpl.class);
    private static final String PROPERTIES_EXTENSION = "properties";

    @Autowired
    @Qualifier("ipfSettings")
    private SettingsFacade settingsFacade;

    @Autowired
    private IPFTemplateDataService ipfTemplateDataService;

    @PostConstruct
    public void init() {
        Properties properties = settingsFacade.getProperties(Constants.IPF_PROPERTIES_FILE);
        if (properties == null || StringUtils.isEmpty(properties.getProperty(Constants.IPF_TEMPLATE_PATH_KEY))) {
            LOGGER.debug("Cannot find {} file or {} is blank.", Constants.IPF_PROPERTIES_FILE, Constants.IPF_TEMPLATE_PATH_KEY);
            return;
        }

        String templatesLocation = properties.getProperty(Constants.IPF_TEMPLATE_PATH_KEY);

        for (File templateFile : getAllTemplateFiles(templatesLocation)) {
            LOGGER.debug("Parsing {} template file", templateFile.getName());
            File propertiesFile = FileUtils.getFile(getPropertiesFileName(templateFile.getPath()));

            // We need properties for template
            if (propertiesFile.exists()) {
                try(InputStream isTemplate = new FileInputStream(templateFile); InputStream isProperties = new FileInputStream(propertiesFile)) {
                    String templateName = getTemplateName(templateFile.getName());
                    Byte[] templateData = ArrayUtils.toObject(IOUtils.toByteArray(isTemplate));
                    Map<String, String> propertiesData = getPropertiesMap(isProperties);

                    IPFTemplate ipfTemplate = ipfTemplateDataService.findByName(templateName);
                    if (ipfTemplate == null) {
                        ipfTemplate = new IPFTemplate(templateName, templateData, propertiesData);
                    }

                    ipfTemplateDataService.createOrUpdate(ipfTemplate);
                } catch (IOException e) {
                    // TODO ? throw exception or continue ?
                    LOGGER.error("Cannot read {} template file or properties", templateFile.getName(), e);
                }
            } else {
                LOGGER.warn("Cannot find properties file for {}, template will not be added", templateFile.getName());
            }
        }
    }

    @Override
    @Transactional
    public List<IPFTemplate> getAllTemplates() {
        return ipfTemplateDataService.retrieveAll();
    }

    @Override
    @Transactional
    public IPFTemplate getTemplateByName(String templateName) {
        return ipfTemplateDataService.findByName(templateName);
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

    private Map<String, String> getPropertiesMap(InputStream isProperties) throws IOException {
        Properties propertiesData = new Properties();
        propertiesData.load(isProperties);
        Map<String, String> propertiesMap = new HashedMap();

        for (Object key : propertiesData.keySet()) {
            String stringKey = (String) key;
            propertiesMap.put(stringKey, propertiesData.getProperty((stringKey)));
        }

        return propertiesMap;
    }

    private String getTemplateName(String templatePath) {
        return templatePath.substring(0, templatePath.length() - 4);
    }

    private String getPropertiesFileName(String templatePath) {
        return templatePath.substring(0, templatePath.length() - 3) + PROPERTIES_EXTENSION;
    }
}
