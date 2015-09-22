package org.motechproject.scheduletracking.domain;

/**
 * The <code>WindowName</code> Enum contains the possible names for
 * a milestone window.
 */
public enum WindowName {

    /**
     * The first window of the milestone.
     */
    earliest,

    /**
     * The second window of the milestone.
     */
    due,

    /**
     * The third window of the milestone.
     */
    late,

    /**
     * The last window of the milestone.
     */
    max;
}
