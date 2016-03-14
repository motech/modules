package org.motechproject.ipf.handler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.motechproject.event.MotechEvent;
import org.motechproject.ipf.event.EventSubjects;
import org.motechproject.ipf.handler.helper.IPFActionHelper;
import org.motechproject.ipf.util.Constants;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class IPFActionHandlerTest {

    @Mock
    private IPFActionHelper ipfActionHelper;

    @InjectMocks
    private IPFActionEventHandler ipfActionEventHandler = new IPFActionEventHandler();

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldHandleEvents() throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException {
        Map<String, Object> params = new HashMap<>();
        params.put(Constants.TEMPLATE_NAME_PARAM, "sampleTemplate2");
        params.put(Constants.RECIPIENT_NAME_PARAM, "sampleRecipient2");
        MotechEvent motechEvent = new MotechEvent(EventSubjects.TEMPLATE_ACTION + ".testTemplate2", params);

        ipfActionEventHandler.handleIpfTaskAction(motechEvent);

        verify(ipfActionHelper).handleAction(params);
    }
}
