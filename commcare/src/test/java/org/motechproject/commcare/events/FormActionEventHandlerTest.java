package org.motechproject.commcare.events;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.motechproject.commcare.domain.FormValueElement;
import org.motechproject.commcare.domain.FormXml;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.commcare.service.CommcareFormService;
import org.motechproject.event.MotechEvent;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.Mockito.verify;


public class FormActionEventHandlerTest {

    private static final String XMLNS = "http://openrosa.org/formdesigner/E6511C2B-DFC8-4DEA-8200-CC2F2CED00DA";
    private static final String CONFIG = "config1";

    @Mock
    private CommcareFormService commcareFormService;

    private MotechEvent event;

    @InjectMocks
    private FormActionEventHandler formActionEventHandler = new FormActionEventHandler();

    @Before
    public void setUp() {
        initMocks(this);
        Map<String, Object> formFields = new HashMap<>();
        formFields.put("/data/mother", "Jane");
        formFields.put("/data/mother/dob_known", "yes");
        formFields.put("/data/mother/dob", "1993-02-29");
        formFields.put("/data/mother/child", "Mark");
        formFields.put("xmlns", XMLNS);

        event = new MotechEvent(EventSubjects.SUBMIT_FORM + "." + XMLNS + "." + CONFIG, formFields);
    }

    @Test
    public void shouldConvertEventParametersAndSubmitForm() {
        formActionEventHandler.submitForm(event);

        ArgumentCaptor<FormXml> captor = ArgumentCaptor.forClass(FormXml.class);
        verify(commcareFormService).uploadForm(captor.capture(), eq(CONFIG));

        FormXml actual = captor.getValue();

        assertEquals(XMLNS, actual.getXmlns());
        assertEquals(1, actual.getFormFields().size());
        FormValueElement element = actual.getFormFields().get(0);

        assertEquals("Jane", element.getValue());
        assertEquals(3, element.getSubElements().size());

        assertEquals("yes", element.getChildElement("dob_known").getValue());
        assertEquals("1993-02-29", element.getChildElement("dob").getValue());
        assertEquals("Mark", element.getChildElement("child").getValue());
    }
}
