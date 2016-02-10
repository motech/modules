package org.motechproject.ipf.service.impl;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.motechproject.ipf.util.Constants;
import org.motechproject.server.config.SettingsFacade;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest({FileUtils.class, IOUtils.class})
public class IPFTemplateServiceTest {

    private static final String TEMPLATES_DIR = "/home/motech/templates";

    @Mock
    File file;

    @Mock
    Properties properties;

    @Mock
    SettingsFacade settingsFacade;

    @InjectMocks
    private IPFTemplateServiceImpl ipfTemplateService = new IPFTemplateServiceImpl();

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldLoadTemplates() throws IOException {
        when(settingsFacade.getProperties(Constants.IPF_PROPERTIES_FILE)).thenReturn(properties);
        when(properties.getProperty(Constants.IPF_TEMPLATE_KEY)).thenReturn(TEMPLATES_DIR);

        PowerMockito.mockStatic(FileUtils.class);
        when(FileUtils.getFile(TEMPLATES_DIR)).thenReturn(file);

        when(file.exists()).thenReturn(true);
        when(file.isDirectory()).thenReturn(true);
        File tempFile1 = File.createTempFile("ipf_motech_", "_template_1");
        File tempFile2 = File.createTempFile("ipf_motech_", "_template_2");

        File[] files = { tempFile1, tempFile2 };
        when(file.listFiles(any(FileFilter.class))).thenReturn(files);

        PowerMockito.mockStatic(IOUtils.class);
        when(IOUtils.toString(any(InputStream.class))).thenReturn("tamplate_data");

        ipfTemplateService.init();

        //remove temp files
        tempFile1.delete();
        tempFile2.delete();

        List<String> templates = ipfTemplateService.getAllTemplateNames();

        assertNotNull(templates);
        assertEquals(2, templates.size());
        assertEquals(tempFile1.getName(), templates.get(0));
        assertEquals(tempFile2.getName(), templates.get(1));

        Mockito.verify(settingsFacade, times(2)).saveRawConfig(anyString(), anyString());
        Mockito.verify(settingsFacade).saveRawConfig(tempFile1.getName(), "tamplate_data");
        Mockito.verify(settingsFacade).saveRawConfig(tempFile2.getName(), "tamplate_data");
    }
}
