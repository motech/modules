package org.motechproject.csd.util;

import junit.framework.Assert;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.csd.constants.CSDConstants;
import org.motechproject.csd.domain.CSD;

import java.io.InputStream;

public class MarshallUtilsTest {

    private CSD csd;
    private String xml;

    @Before
    public void setup() throws Exception {
        InputStream in = getClass().getResourceAsStream("/initialXml.xml");
        xml = IOUtils.toString(in);
        csd = InitialData.getInitialData();
    }

    @Test
    public void marshallerTest() throws Exception {
        String generatedXml = MarshallUtils.marshall(csd, CSDConstants.CSD_SCHEMA, CSD.class);
        Assert.assertEquals(xml, generatedXml);
    }

    @Test
    public void unmarshallerTest() throws Exception {
        CSD generatedCSD = (CSD) MarshallUtils.unmarshall(xml, CSDConstants.CSD_SCHEMA, CSD.class);
        Assert.assertEquals(csd, generatedCSD);
    }
}
