package org.motechproject.commcare.tasks.builder;

import org.motechproject.tasks.contract.TriggerEventRequest;

import java.util.List;

public interface TriggerBuilder {

    List<TriggerEventRequest> buildTriggers();
}
