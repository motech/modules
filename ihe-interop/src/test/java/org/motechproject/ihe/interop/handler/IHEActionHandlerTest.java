package org.motechproject.ihe.interop.handler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.motechproject.event.MotechEvent;
import org.motechproject.ihe.interop.event.EventSubjects;
import org.motechproject.ihe.interop.handler.helper.IHEActionHelper;
import org.motechproject.ihe.interop.util.Constants;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class IHEActionHandlerTest {

    @Mock
    private IHEActionHelper iheActionHelper;

    @InjectMocks
    private IHEActionEventHandler iheActionEventHandler = new IHEActionEventHandler();

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

        iheActionEventHandler.handleIheTaskAction(motechEvent);

        verify(iheActionHelper).handleAction(params);
    }
}
