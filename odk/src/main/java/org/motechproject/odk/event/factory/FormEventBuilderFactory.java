package org.motechproject.odk.event.factory;

import org.motechproject.odk.domain.ConfigurationType;
import org.motechproject.odk.event.builder.EventBuilder;
import org.motechproject.odk.event.builder.impl.EventBuilderODK;
import org.motechproject.odk.event.builder.impl.EventBuilderOna;

/**
 * Factory class for {@link org.motechproject.odk.event.builder.EventBuilder}
 */
public class FormEventBuilderFactory {

    /**
     * Returns the appropriate event builder based on the configuration type.
     * @param type
     * @return {@link EventBuilder}
     */
    public EventBuilder getBuilder(ConfigurationType type) {
        switch (type) {
            case ODK:
                return new EventBuilderODK();
            case ONA:
                return new EventBuilderOna();
            case KOBO:
                return new EventBuilderOna();
            default:
                return null;
        }
    }
}
