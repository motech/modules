package org.motechproject.cmslite.api.osgi;

import org.apache.http.impl.client.BasicResponseHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.cmslite.api.model.CMSLiteException;
import org.motechproject.cmslite.api.model.ContentNotFoundException;
import org.motechproject.cmslite.api.model.StringContent;
import org.motechproject.cmslite.api.service.CMSLiteService;
import org.motechproject.config.service.ConfigurationService;
import org.motechproject.event.listener.EventListenerRegistryService;
import org.motechproject.server.config.SettingsFacade;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.TestContext;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

import javax.inject.Inject;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class CMSLiteBundleIT extends BasePaxIT {

    @Inject
    private EventListenerRegistryService eventListenerRegistryService;
    @Inject
    private SettingsFacade settingsFacade;
    @Inject
    private ConfigurationService configurationService;
    @Inject
    private CMSLiteService cmsLiteService;

    @Test
    public void testCMSLiteApiBundle() throws CMSLiteException, ContentNotFoundException, IOException, InterruptedException {
        assertNotNull(eventListenerRegistryService);
        assertNotNull(settingsFacade);
        assertNotNull(configurationService);
        assertNotNull(cmsLiteService);

        cmsLiteService.addContent(new StringContent("en", "title", "Test content"));

        final StringContent content = cmsLiteService.getStringContent("en", "title");
        assertEquals("Test content", content.getValue());

        String response = getHttpClient().get(String.format("http://localhost:%d/cmsliteapi/string/en/title",
                TestContext.getJettyPort()), new BasicResponseHandler());

        assertEquals("Test content", response);
    }
}