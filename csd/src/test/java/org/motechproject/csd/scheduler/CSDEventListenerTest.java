package org.motechproject.csd.scheduler;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.csd.constants.CSDEventKeys;
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

    @Mock
    private CSDScheduler csdScheduler;

    private CSDEventListener csdEventListener;

    private String url;
    private String xml;

    @Before
    public void setup() throws Exception {
        url = "someURL";
        xml = "someXml";

        initMocks(this);

        when(csdHttpClient.getXml(url)).thenReturn(xml);
        csdEventListener = new CSDEventListener(csdService, csdScheduler);
    }

    @Test
    public void shouldGetXmlFromUrlAndSaveItIntoTheDatabase() {
        Map<String, Object> eventParameters = new HashMap<>();
        eventParameters.put(CSDEventKeys.XML_URL, url);

        MotechEvent event = new MotechEvent(CSDEventKeys.CONSUME_XML_EVENT_BASE + "123", eventParameters);

        csdEventListener.consumeXml(event);

        verify(csdService).fetchAndUpdate(url);
        verify(csdScheduler).sendScheduledUpdateEventMessage(url);
    }

    @Test
    public void verifyTaskUpdateUsingREST() {
        Map<String, Object> eventParameters = new HashMap<>();
        eventParameters.put(CSDEventKeys.XML_URL, url);

        MotechEvent event = new MotechEvent(CSDEventKeys.CSD_TASK_REST_UPDATE, eventParameters);

        csdEventListener.taskUpdateUsingREST(event);

        verify(csdService).fetchAndUpdateUsingREST(url);
        verify(csdScheduler).sendTaskUpdateEventMessage(url);
    }

    @Test
    public void verifyTaskUpdateUsingSOAP() {
        Map<String, Object> eventParameters = new HashMap<>();
        String lastModified = new DateTime().toString();
        eventParameters.put(CSDEventKeys.XML_URL, url);
        eventParameters.put(CSDEventKeys.LAST_MODIFIED, lastModified);

        MotechEvent event = new MotechEvent(CSDEventKeys.CSD_TASK_SOAP_UPDATE, eventParameters);

        csdEventListener.taskUpdateUsingSOAP(event);

        verify(csdService).fetchAndUpdateUsingSOAP(url, DateTime.parse(lastModified));
        verify(csdScheduler).sendTaskUpdateEventMessage(url);
    }
}
