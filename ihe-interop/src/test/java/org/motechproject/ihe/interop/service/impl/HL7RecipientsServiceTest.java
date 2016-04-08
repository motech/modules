package org.motechproject.ihe.interop.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.motechproject.config.SettingsFacade;
import org.motechproject.ihe.interop.domain.HL7Recipient;
import org.motechproject.ihe.interop.util.Constants;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class HL7RecipientsServiceTest {

    @Mock
    private SettingsFacade settingsFacade;

    @InjectMocks
    private HL7RecipientsServiceImpl hl7RecipientsService = new HL7RecipientsServiceImpl();

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldLoadRecipients() {
        when(settingsFacade.getRawConfig(Constants.HL7_RECIPIENTS_FILE)).thenReturn(getClass().getResourceAsStream("/hl7-recipients.json"));

        hl7RecipientsService.init();

        Collection<HL7Recipient> recipients = hl7RecipientsService.getAllRecipients();

        assertNotNull(recipients);
        assertEquals(2, recipients.size());

        HL7Recipient rec1 = hl7RecipientsService.getRecipientbyName("test_recipient_1");
        HL7Recipient rec2 = hl7RecipientsService.getRecipientbyName("test_recipient_2");
        HL7Recipient rec3 = hl7RecipientsService.getRecipientbyName("test_recipient_3");

        assertNotNull(rec1);
        assertEquals("test_recipient_1", rec1.getRecipientName());
        assertEquals("test_url_1", rec1.getRecipientUrl());

        assertNotNull(rec2);
        assertEquals("test_recipient_2", rec2.getRecipientName());
        assertEquals("test_url_2", rec2.getRecipientUrl());

        assertNull(rec3);
    }
}
