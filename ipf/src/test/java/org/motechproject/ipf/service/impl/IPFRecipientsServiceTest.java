package org.motechproject.ipf.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.motechproject.ipf.domain.IPFRecipient;
import org.motechproject.ipf.util.Constants;
import org.motechproject.config.SettingsFacade;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class IPFRecipientsServiceTest {

    @Mock
    private SettingsFacade settingsFacade;

    @InjectMocks
    private IPFRecipientsServiceImpl ipfRecipientsService = new IPFRecipientsServiceImpl();

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldLoadRecipients() {
        when(settingsFacade.getRawConfig(Constants.IPF_RECIPIENTS_FILE)).thenReturn(getClass().getResourceAsStream("/ipf-recipients.json"));

        ipfRecipientsService.init();

        Collection<IPFRecipient> recipients = ipfRecipientsService.getAllRecipients();

        assertNotNull(recipients);
        assertEquals(2, recipients.size());

        IPFRecipient rec1 = ipfRecipientsService.getRecipientbyName("test_recipient_1");
        IPFRecipient rec2 = ipfRecipientsService.getRecipientbyName("test_recipient_2");
        IPFRecipient rec3 = ipfRecipientsService.getRecipientbyName("test_recipient_3");

        assertNotNull(rec1);
        assertEquals("test_recipient_1", rec1.getRecipientName());
        assertEquals("test_url_1", rec1.getRecipientUrl());

        assertNotNull(rec2);
        assertEquals("test_recipient_2", rec2.getRecipientName());
        assertEquals("test_url_2", rec2.getRecipientUrl());

        assertNull(rec3);
    }
}
