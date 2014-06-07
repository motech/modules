package org.motechproject.sms.configs;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.sms.SmsEventSubjects;
import org.motechproject.sms.audit.DeliveryStatus;

import static org.junit.Assert.assertEquals;

public class ConfigTest {

    private static final int failureCount = 3;
    private Config config;

    @Before
    public void setup() {
        config = new Config();
        config.setMaxRetries(failureCount);
    }

    @Test
    public void shouldReturnRetryThenAbortSubject() {
        assertEquals(SmsEventSubjects.RETRYING, config.retryOrAbortSubject(failureCount - 1));
        assertEquals(SmsEventSubjects.ABORTED, config.retryOrAbortSubject(failureCount));
    }

    @Test
    public void shouldReturnRetryThenAbortStatus() {
        assertEquals(DeliveryStatus.RETRYING, config.retryOrAbortStatus(failureCount - 1));
        assertEquals(DeliveryStatus.ABORTED, config.retryOrAbortStatus(failureCount));
    }
}
