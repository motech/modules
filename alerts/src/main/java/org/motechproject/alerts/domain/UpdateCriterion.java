package org.motechproject.alerts.domain;

/**
 * An update criterion that describes which field of an {@link org.motechproject.alerts.domain.Alert}
 * should be updated.
 */
public enum UpdateCriterion {
    STATUS,
    NAME,
    DESCRIPTION,
    PRIORITY,
    DATA
}
