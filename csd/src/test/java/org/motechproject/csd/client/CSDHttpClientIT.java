package org.motechproject.csd.client;

import junit.framework.Assert;
import org.junit.Test;

/**
 * Verify that CSDHttpClient is present & functional.
 */
public class CSDHttpClientIT {

    @Test
    public void verifyCSDHttpClient() throws Exception {
        CSDHttpClient csdHttpClient = new CSDHttpClient();

        String xml = csdHttpClient.getXml("https://raw.githubusercontent.com/openhie/openinfoman/master/resources/service_directories/CSD-Providers-Connectathon-20150120.xml");

        Assert.assertNotNull(xml);
    }
}
