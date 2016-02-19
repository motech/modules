package org.motechproject.commcare.tasks.builder;

import org.motechproject.tasks.contract.ActionEventRequest;

import java.util.List;

/**
 * Provides method for building task actions.
 */
public interface ActionBuilder {

    /**
     * Builds a list of actions that can be used for creating channel for the Task module.
     *
     * @return the list of actions
     */
    List<ActionEventRequest> buildActions();
}
