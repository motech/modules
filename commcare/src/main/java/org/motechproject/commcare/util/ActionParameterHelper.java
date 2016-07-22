package org.motechproject.commcare.util;

import org.motechproject.commcare.events.constants.DisplayNames;
import org.motechproject.commcare.events.constants.EventDataKeys;
import org.motechproject.tasks.contract.ActionParameterRequest;
import org.motechproject.tasks.contract.builder.ActionParameterRequestBuilder;

/**
 * Created by root on 22.07.16.
 */
public final class ActionParameterHelper {

    public static ActionParameterRequest createConfigNameParameter(String configName, int order) {
        ActionParameterRequestBuilder builder;

        builder = new ActionParameterRequestBuilder()
                .setDisplayName(DisplayNames.CONFIG_NAME)
                .setKey(EventDataKeys.CONFIG_NAME)
                .setValue(configName)
                .setRequired(true)
                .setHidden(true)
                .setOrder(order);

        return builder.createActionParameterRequest();
    }

    private ActionParameterHelper() {
    }
}
