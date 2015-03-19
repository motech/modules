package org.motechproject.csd.scheduler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.csd.CSDEventKeys;
import org.motechproject.csd.client.CSDHttpClient;
import org.motechproject.csd.service.CSDService;
import org.motechproject.event.MotechEvent;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class CSDEventListenerTest {

    @Mock
    private CSDHttpClient csdHttpClient;

    @Mock
    private CSDService csdService;

    private CSDEventListener csdEventListener;

    private String url;
    private String xml;

    @Before
    public void setup() throws Exception {
        url = "someURL";
        xml = "someXml";

        initMocks(this);

        when(csdHttpClient.getXml(url)).thenReturn(xml);
        csdEventListener = new CSDEventListener(csdHttpClient, csdService);
    }

    @Test
    public void shouldGetXmlFromUrlAndSaveItIntoTheDatabase() {
        Map<String, Object> eventParameters = new HashMap<>();
        eventParameters.put(CSDEventKeys.XML_URL, url);

        MotechEvent event = new MotechEvent(CSDEventKeys.CONSUME_XML_EVENT, eventParameters);

        csdEventListener.consumeXml(event);

        verify(csdService).saveFromXml(xml);
    }
}
