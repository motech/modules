package org.motechproject.cmslite.web;

import java.io.Serializable;
import java.util.List;

/**
 * A collection of {@link org.motechproject.cmslite.web.ResourceDto} objects to be displayed on the jqgrid.
 * It also contains paging information for the grid.
 */
public class Resources implements Serializable {
    private static final long serialVersionUID = -6205245415683301270L;

    /**
     * The page that is being displayed.
     */
    private final Integer page;

    /**
     * The total number of pages available.
     */
    private final Integer total;

    /**
     * The total number of records in the system.
     */
    private final Integer records;

    /**
     * List of {@link org.motechproject.cmslite.web.ResourceDto} instances that will displayed on the grid.
     */
    private final List<ResourceDto> rows;

    /**
     * Constructs an instance from the list of resources (by creating a sub-list) and the given grid settings.
     * @param settings the grid settings to use
     * @param list the list of all available records
     */
    public Resources(GridSettings settings, List<ResourceDto> list) {
        this.page = settings.getPage();
        records = list.size();
        total = records <= settings.getRows() ? 1 : (records / settings.getRows()) + 1;

        Integer start = settings.getRows() * (page > total ? total : page) - settings.getRows();
        Integer count = start + settings.getRows();
        Integer end = count > records ? records : count;

        this.rows = list.subList(start, end);
    }

    /**
     * @return the page that is being displayed
     */
    public Integer getPage() {
        return page;
    }

    /**
     * @return the total number of pages available
     */
    public Integer getTotal() {
        return total;
    }

    /**
     * @return the total number of records in the system
     */
    public Integer getRecords() {
        return records;
    }

    /**
     * @return list of {@link org.motechproject.cmslite.web.ResourceDto} instances that will displayed on the grid
     */
    public List<ResourceDto> getRows() {
        return rows;
    }

    @Override
    public String toString() {
        return String.format("Resources{page=%d, total=%d, records=%d, rows=%s}", page, total, records, rows);
    }
}
