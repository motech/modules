package org.motechproject.pillreminder.builder;

import org.motechproject.scheduler.service.MotechSchedulerService;
import org.motechproject.pillreminder.EventKeys;

import java.util.HashMap;
import java.util.Map;

/**
 * Builds payloads for events scheduled using {@link MotechSchedulerService}.
 * One instance of this builder should be used for constructing one payload.
 */
public class SchedulerPayloadBuilder {

    private Map<String, Object> params = new HashMap<>();

    /**
     * Places the provided id as the job ID in the payload, this id will be used
     * by the scheduler for scheduling the job.
     * @param id the job id to use
     * @return this instance of the builder
     */
    public SchedulerPayloadBuilder withJobId(String id) {
        params.put(MotechSchedulerService.JOB_ID_KEY, id);
        return this;
    }

    /**
     * Places the provided dosage ID in the payload, under {@link EventKeys#DOSAGE_ID_KEY}.
     * @param id the dosage id to put into the payload
     * @return this instance of the builder
     */
    public SchedulerPayloadBuilder withDosageId(Long id) {
        params.put(EventKeys.DOSAGE_ID_KEY, id);
        return this;
    }

    /**
     * Places the provided external ID in the payload under {@link EventKeys#EXTERNAL_ID_KEY}.
     * @param id the external id to put into the payload
     * @return this instance of the builder
     */
    public SchedulerPayloadBuilder withExternalId(String id) {
        params.put(EventKeys.EXTERNAL_ID_KEY, id);
        return this;
    }

    /**
     * Returns the payload constructed by this builder, based on previous method calls.
     * @return the event payload
     */
    public Map<String, Object> payload() {
        return params;
    }
}
