package org.motechproject.sms.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.motechproject.server.config.SettingsFacade;
import org.motechproject.sms.audit.SmsAuditService;
import org.motechproject.sms.templates.TemplateReader;

import java.util.Arrays;

import static org.mockito.MockitoAnnotations.initMocks;

public class SmsServiceTest {
    @Mock
    private EventRelay eventRelay;
    @Mock
    MotechSchedulerService schedulerService;
    @Mock
    TemplateReader templateReader;
    @Mock
    SmsAuditService smsAuditService;

    private SmsService smsSender;

    @Before
    public void setUp() {
        initMocks(this);
        //when(templateReader.getTemplates()).thenReturn(new Templates(settings, new ArrayList<Template>()));

        SettingsFacade settings = new SettingsFacade();
        //settings.saveConfigProperties("ivr.properties", ivrProperties);

        //smsSender = new SmsServiceImpl(settings, eventRelay, schedulerService, templateReader, smsAuditService);
    }
    @Test
    public void shouldSendSms() throws Exception {
        OutgoingSms outgoingSms = new OutgoingSms(Arrays.asList(new String[]{"+12065551212"}), "sample message");
        //smsSender.send(outgoingSms);
    }
}
