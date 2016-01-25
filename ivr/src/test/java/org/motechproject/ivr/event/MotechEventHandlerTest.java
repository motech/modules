package org.motechproject.ivr.event;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.motechproject.event.MotechEvent;
import org.motechproject.ivr.service.OutboundCallService;
import org.motechproject.scheduler.contract.RunOnceSchedulableJob;
import org.motechproject.scheduler.service.MotechSchedulerService;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.motechproject.ivr.event.EventParams.CALL_DATE;
import static org.motechproject.ivr.event.EventParams.CONFIG;
import static org.motechproject.ivr.event.EventParams.PARAMETERS;

public class MotechEventHandlerTest {

    private static final String CONFIG_NAME = "FooConfig";

    @Mock
    private OutboundCallService outboundCallService;

    @Mock
    private MotechSchedulerService schedulerService;

    @Captor
    private ArgumentCaptor<Map<String, String>> parametersCaptor;

    @Captor
    private ArgumentCaptor<RunOnceSchedulableJob> jobCaptor;

    @Captor
    private ArgumentCaptor<String> configCaptor;

    private MotechEventHandler eventHandler;

    @Before
    public void setUp() {
        initMocks(this);
        eventHandler = new MotechEventHandler(outboundCallService, schedulerService);
        doNothing().when(outboundCallService).initiateCall(anyString(), anyMap());
        doNothing().when(schedulerService).scheduleRunOnceJob(any(RunOnceSchedulableJob.class));
    }

    @Test
    public void shouldScheduleRunOnceJobIfDateIsSet() {

        Map<String, Object> params = prepareParams();
        params.put(CALL_DATE, DateTime.now());

        RunOnceSchedulableJob expectedJob = prepareJob(params);
        MotechEvent event = new MotechEvent(EventSubjects.INITIATE_CALL, params);

        eventHandler.handleExternal(event);

        verify(schedulerService, times(1)).scheduleRunOnceJob(jobCaptor.capture());
        verify(outboundCallService, times(0)).initiateCall(anyString(), anyMap());

        assertEquals(expectedJob, jobCaptor.getValue());
    }

    @Test
    public void shouldInitiateCallIfDateIsNotSet() {

        Map<String, Object> params = prepareParams();

        MotechEvent event = new MotechEvent(EventSubjects.INITIATE_CALL, params);

        eventHandler.handleExternal(event);

        verify(outboundCallService, times(1)).initiateCall(configCaptor.capture(), parametersCaptor.capture());
        verify(schedulerService, times(0)).scheduleRunOnceJob(any(RunOnceSchedulableJob.class));

        assertEquals(CONFIG_NAME, configCaptor.getValue());
        assertEquals(params.get(PARAMETERS), parametersCaptor.getValue());
    }

    private RunOnceSchedulableJob prepareJob(Map<String, Object> params) {
        return new RunOnceSchedulableJob(prepareExpectedEvent(params), ((DateTime) params.get(CALL_DATE)));
    }

    private MotechEvent prepareExpectedEvent(Map<String, Object> params) {

        Map<String, Object> eventParams = new HashMap<>();
        eventParams.put(CONFIG, CONFIG_NAME);
        eventParams.put(PARAMETERS, params.get(PARAMETERS));

        return new MotechEvent(EventSubjects.INITIATE_CALL, eventParams);
    }

    private Map<String, Object> prepareParams() {

        Map<String, Object> params = new HashMap<>();

        params.put(CONFIG, CONFIG_NAME);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("key1", "value1");

        params.put(PARAMETERS, parameters);

        return params;
    }
}
