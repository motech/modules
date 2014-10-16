package org.motechproject.hub.mds.service.it;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.hub.mds.HubDistributionContent;
import org.motechproject.hub.mds.service.HubDistributionContentMDSService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;
import org.springframework.http.MediaType;

import javax.inject.Inject;
import java.util.List;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class HubDistributionContentMDSServiceBundleIT extends BasePaxIT {

    @Inject
    private HubDistributionContentMDSService hubDistributionContentMDSService;

    private String contentType = MediaType.APPLICATION_XML.toString();

    @Test
    public void testHubDistributionContent() {
        List<HubDistributionContent> hubDistributionContents = hubDistributionContentMDSService
                .retrieveAll();
        Assert.assertNotNull(hubDistributionContents);

        HubDistributionContent hubDistributioncontent = new HubDistributionContent();
        hubDistributioncontent.setContent("content");
        hubDistributioncontent.setContentType(contentType);

        hubDistributionContentMDSService.create(hubDistributioncontent);

        hubDistributionContents = hubDistributionContentMDSService
                .retrieveAll();
        Assert.assertNotNull(hubDistributionContents);

        hubDistributionContentMDSService.delete(hubDistributioncontent);

    }
}
