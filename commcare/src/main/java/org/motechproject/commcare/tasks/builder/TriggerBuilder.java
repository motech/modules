package org.motechproject.commcare.tasks.builder;

import org.motechproject.tasks.contract.TriggerEventRequest;

import java.util.List;

/**
 * Provides method for building task triggers.
 */
public interface TriggerBuilder {

    /**
     * Builds a list of triggers that can be used for creating channel for the Task module.
     *
     * @return the list of triggers
     */
    List<TriggerEventRequest> buildTriggers();
}
