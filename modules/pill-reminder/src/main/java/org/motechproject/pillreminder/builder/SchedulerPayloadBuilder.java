package org.motechproject.pillreminder.builder;

import org.motechproject.scheduler.service.MotechSchedulerService;
import org.motechproject.pillreminder.EventKeys;

import java.util.HashMap;
import java.util.Map;

public class SchedulerPayloadBuilder {
    private Map<String, Object> params = new HashMap<String, Object>();

    public SchedulerPayloadBuilder withJobId(String id) {
        params.put(MotechSchedulerService.JOB_ID_KEY, id);
        return this;
    }

    public SchedulerPayloadBuilder withDosageId(String id) {
        params.put(EventKeys.DOSAGE_ID_KEY, id);
        return this;
    }

    public SchedulerPayloadBuilder withExternalId(String id) {
        params.put(EventKeys.EXTERNAL_ID_KEY, id);
        return this;
    }

    public Map<String, Object> payload() {
        return params;
    }
}
