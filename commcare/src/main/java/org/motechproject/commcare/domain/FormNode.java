package org.motechproject.commcare.domain;

/**
 * Interface for form nodes. Form node can be either form element or form attribute.
 */
public interface FormNode {

    String PREFIX_SEARCH_RELATIVE = "//";
    String PREFIX_SEARCH_FROM_ROOT = "/";
    String PREFIX_VALUE = "#";
    String PREFIX_ATTRIBUTE = "@";

    /**
     * Returns the value of this node.
     *
     * @return the value of this node
     */
    String getValue();
}
