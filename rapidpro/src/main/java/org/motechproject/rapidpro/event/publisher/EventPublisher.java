package org.motechproject.rapidpro.event.publisher;

import org.motechproject.rapidpro.webservice.dto.Contact;
import org.motechproject.rapidpro.webservice.dto.Group;

/**
 * Publishes events occuring in the rapidpro module.
 */
public interface EventPublisher {

    /**
     * Publishes an {@link org.motechproject.event.MotechEvent} with subject
     * org.motechproject.rapidpro.contact-added-to-group
     * @param externalId The external ID of the contact added to the group
     * @param contact The contact added to the group.
     * @param group The group the contact was added to.
     */
    void publishContactAddedToGroup(String externalId, Contact contact, Group group);

    /**
     * Publishes an {@link org.motechproject.event.MotechEvent} with subject
     * org.motechproject.rapidpro.contact-removed-from-group
     * @param externalId  The external ID of the contact removed from the group
     * @param contact The contact removed from the group
     * @param group The group the contact was removed from.
     */
    void publishContactRemovedFromGroup(String externalId, Contact contact, Group group);

}
