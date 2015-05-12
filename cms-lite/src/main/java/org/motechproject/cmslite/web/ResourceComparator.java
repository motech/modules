package org.motechproject.cmslite.web;

import java.io.Serializable;
import java.util.Comparator;

/**
 * The comparator used for ordering content for the UI grid. It can compare
 * content by either using the content name or content type.
 */
public class ResourceComparator implements Comparator<ResourceDto>, Serializable {
    private static final long serialVersionUID = -3442591167945003657L;

    private final String field;
    private final boolean descending;

    /**
     * Constructs an instance using taking the sorting settings from the provided grid settings.
     * @param settings grid settings based on which this comparator will be created
     */
    public ResourceComparator(GridSettings settings) {
        this.field = settings.getSortColumn();
        this.descending = settings.isDescending();
    }

    @Override
    public int compare(ResourceDto first, ResourceDto second) {
        int compare;

        switch (field) {
            case "name":
                compare = first.getName().compareToIgnoreCase(second.getName());
                break;
            case "type":
                compare = first.getType().compareToIgnoreCase(second.getType());
                break;
            default:
                compare = 0;
        }

        return compare * (descending ? -1 : 1);
    }
}
