package org.motechproject.csd.util;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.csd.constants.CSDConstants;
import org.motechproject.csd.db.InitialData;
import org.motechproject.csd.domain.CSD;

import java.io.InputStream;

import static junit.framework.Assert.assertEquals;

public class MarshallUtilsTest {

    private CSD csd;
    private String xml;

    @Before
    public void setup() throws Exception {
        try (InputStream in = getClass().getResourceAsStream("/initialXml.xml")) {
            xml = IOUtils.toString(in);
            xml = xml.replace("\r\n", "\n"); // remove windows line feeds
            csd = InitialData.getInitialData();
        }
    }

    @Test
    public void marshallerTest() throws Exception {
        String generatedXml = MarshallUtils.marshall(csd, CSDConstants.CSD_SCHEMA, CSD.class);
        assertEquals(xml, generatedXml);
    }

    @Test
    public void unmarshallerTest() throws Exception {
        CSD generatedCSD = (CSD) MarshallUtils.unmarshall(xml, CSDConstants.CSD_SCHEMA, CSD.class);
        assertEquals(csd, generatedCSD);
    }
}
