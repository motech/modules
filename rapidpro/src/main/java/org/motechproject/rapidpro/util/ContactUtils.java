package org.motechproject.rapidpro.util;

import org.motechproject.rapidpro.constant.EventParameters;
import org.motechproject.rapidpro.webservice.dto.Contact;

import java.util.Map;

/**
 * Utility class for common operations with {@link Contact}.
 */
public final class ContactUtils {

    private static final String TEL_PREFIX = "tel:";
    private static final char PLUS = '+';

    private ContactUtils() {
    }

    /**
     * Creates a new {@link Contact} from {@link org.motechproject.event.MotechEvent} parameters.
     *
     * @param params The parameters from a {@link org.motechproject.event.MotechEvent}
     * @return {@link Contact}
     */
    public static Contact toContactFromParams(Map<String, Object> params) {
        String name = (String) params.get(EventParameters.NAME);
        String language = (String) params.get(EventParameters.LANGUAGE);
        String phone = (String) params.get(EventParameters.PHONE);
        Map<String, String> fields = (Map<String, String>) params.get(EventParameters.FIELDS);
        boolean blocked = (boolean) (params.get(EventParameters.BLOCKED) == null ? false : params.get(EventParameters.BLOCKED));
        boolean failed = (boolean) (params.get(EventParameters.FAILED) == null ? false : params.get(EventParameters.FAILED));

        Contact contact = new Contact();
        contact.setName(name);
        contact.setLanguage(language);
        contact.setFields(fields);
        contact.setBlocked(blocked);
        contact.setFailed(failed);

        if (phone != null) {
            contact.getUrns().add(createPhoneUrn(phone));
        }

        return contact;
    }

    /**
     * Merges the fields from a Contact returned from Rapidpro to a {@link Contact} representing a Contact with
     * updated fields. Sets the UUID of updated to the UUID of the contact object from rapidpro. Sets the group UUIDs field to
     * the group UUIDs field of the contact object from rapidpro
     *
     * @param updated      The {@link Contact} with updated fields.
     * @param fromRapidpro The {@link Contact} from Rapidpro.
     */
    public static void mergeContactFields(Contact updated, Contact fromRapidpro) {
        updated.setUuid(fromRapidpro.getUuid());
        updated.setGroupUUIDs(fromRapidpro.getGroupUUIDs());
    }

    private static String createPhoneUrn(String phoneNumber) {
        if (phoneNumber.charAt(0) == PLUS) {
            return TEL_PREFIX + phoneNumber;
        } else {
            return TEL_PREFIX + PLUS + phoneNumber;
        }
    }
}
