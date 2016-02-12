package org.motechproject.ipf.service.impl;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.motechproject.ipf.domain.IPFTemplate;
import org.motechproject.ipf.service.IPFTemplateDataService;
import org.motechproject.ipf.util.Constants;
import org.motechproject.server.config.SettingsFacade;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class IPFTemplateServiceTest {

    private static final String TEMPLATES_DIR = System.getProperty("user.home") + File.separator + ".motech" + File.separator + "templates";

    @Mock
    File tempDirectory;

    @Mock
    Properties properties;

    @Mock
    SettingsFacade settingsFacade;

    @Mock
    IPFTemplateDataService ipfTemplateDataService;

    @InjectMocks
    private IPFTemplateServiceImpl ipfTemplateService = new IPFTemplateServiceImpl();

    @Before
    public void setUp() {
        initMocks(this);
        when(settingsFacade.getProperties(Constants.IPF_PROPERTIES_FILE)).thenReturn(properties);
        when(properties.getProperty(Constants.IPF_TEMPLATE_PATH_KEY)).thenReturn(TEMPLATES_DIR);

    }

    @Test
    public void shouldLoadTemplates() throws IOException {
        File[] files = generateFiles();

        ipfTemplateService.init();
        deleteFiles(files);

        Mockito.verify(ipfTemplateDataService, times(2)).createOrUpdate(any(IPFTemplate.class));
        Mockito.verify(ipfTemplateDataService, times(2)).findByName(anyString());
        Mockito.verify(ipfTemplateDataService).findByName("IPF_motech_template_1");
        Mockito.verify(ipfTemplateDataService).findByName("IPF_motech_template_2");
    }

    @Test
    public void shouldLoadTemplateData() throws IOException {
        File tempFile = new File(TEMPLATES_DIR + File.separator + "IPF_motech_template_1.xml");
        File propsFile = new File(TEMPLATES_DIR + File.separator + "IPF_motech_template_1.properties");
        tempFile.getParentFile().mkdirs();
        tempFile.createNewFile();
        propsFile.createNewFile();

        byte[] templateData = IOUtils.toByteArray(getClass().getResourceAsStream("/IPF_test_template.xml"));
        IOUtils.write(templateData, new FileOutputStream(tempFile));
        byte[] propertiesData = IOUtils.toByteArray(getClass().getResourceAsStream("/IPF_test_template.properties"));
        IOUtils.write(propertiesData, new FileOutputStream(propsFile));

        ipfTemplateService.init();
        deleteFiles(new File[] { tempFile, propsFile });

        ArgumentCaptor<IPFTemplate> ipfTemplateArgumentCaptor = ArgumentCaptor.forClass(IPFTemplate.class);
        Mockito.verify(ipfTemplateDataService).createOrUpdate(ipfTemplateArgumentCaptor.capture());

        IPFTemplate ipfTemplate = ipfTemplateArgumentCaptor.getValue();

        assertNotNull(ipfTemplate);
        assertEquals("IPF_motech_template_1", ipfTemplate.getTemplateName());
        assertTrue(Arrays.equals(templateData, ArrayUtils.toPrimitive(ipfTemplate.getTemplateData())));

        Map<String, String> propertiesMap = ipfTemplate.getProperties();
        assertEquals(2, propertiesMap.size());
        assertEquals("Name", propertiesMap.get("name"));
        assertEquals("Postal Code", propertiesMap.get("code"));
    }

    private File[] generateFiles() throws IOException {
        File tempFile1 = new File(TEMPLATES_DIR + File.separator + "IPF_motech_template_1.xml");
        File tempFile2 = new File(TEMPLATES_DIR + File.separator + "IPF_motech_template_2.xml");
        File tempFile3 = new File(TEMPLATES_DIR + File.separator + "IPF_motech_template_3.xml");
        File propsFile1 = new File(TEMPLATES_DIR + File.separator + "IPF_motech_template_1.properties");
        File propsFile2 = new File(TEMPLATES_DIR + File.separator + "IPF_motech_template_2.properties");

        File[] files = new File[] { tempFile1, tempFile2, tempFile3, propsFile1, propsFile2 };
        deleteFiles(files);

        tempFile1.getParentFile().mkdirs();
        tempFile1.createNewFile();
        tempFile2.createNewFile();
        tempFile3.createNewFile();
        propsFile1.createNewFile();
        propsFile2.createNewFile();

        return files;
    }

    private void deleteFiles(File[] files) {
        for (File file : files) {
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
