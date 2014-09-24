package org.motechproject.commcare.tasks;

import org.motechproject.tasks.contract.TriggerEventRequest;

import java.util.List;

public interface TriggerBuilder {

    List<TriggerEventRequest> buildTriggers();
}
