package org.motechproject.cmslite.web;

import static java.util.Arrays.asList;
import static org.apache.commons.lang.StringUtils.equalsIgnoreCase;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.apache.commons.lang.StringUtils.startsWithIgnoreCase;

/**
 * The grid settings coming from the jqgrid plugin.
 */
public class GridSettings {

    /**
     * The default number of rows displayed in the grid.
     */
    public static final int DEFAULT_ROWS = 5;

    /**
     * The languages for which we should filter contents.
     */
    private String languages = "";

    /**
     * The name for which we should filter contents.
     */
    private String name = "";

    /**
     * The name of the field on which sorting should be performed.
     */
    private String sortColumn = "";

    /**
     * The direction of sorting, either asc ord desc.
     */
    private String sortDirection = "asc";

    /**
     * Should stream content be displayed.
     */
    private Boolean stream = true;

    /**
     * Should string content be displayed.
     */
    private Boolean string = true;

    /**
     * The page to display, starting from 1.
     */
    private Integer page = 1;

    /**
     * The number of rows displayed on a page.
     */
    private Integer rows = DEFAULT_ROWS;

    /**
     * Checks whether given parameters from a content match these grid settings.
     * @param contentName the name of the content
     * @param contentLanguage the language of the content
     * @param contentType the type of the content (stream or string)
     * @return true if the content matches these grid settings, false otherwise
     */
    public boolean isCorrect(String contentName, String contentLanguage, String contentType) {
        boolean equalLanguage = isBlank(languages) || containsLanguage(contentLanguage);
        boolean equalName = isBlank(getName()) || startsWithIgnoreCase(contentName, getName());
        boolean equalString = isString() && equalsIgnoreCase(contentType, "string");
        boolean equalStream = isStream() && equalsIgnoreCase(contentType, "stream");

        return equalLanguage && equalName && (equalString || equalStream);
    }

    /**
     * Checks whether the sorting direction for these settings is descending (equal to "desc").
     * @return true if ordering direction is descending, false otherwise
     */
    public boolean isDescending() {
        return equalsIgnoreCase(sortDirection, "desc");
    }

    /**
     * Checks whether the given language matches these grid settings and should be displayed.
     * @param language the language to check
     * @return true if it matches the settings, false otherwise
     */
    public boolean containsLanguage(String language) {
        return asList(languages.split(",")).contains(language);
    }

    /**
     * @return the languages for which we should filter contents
     */
    public String getLanguages() {
        return languages;
    }

    /**
     * Sets the languages for which we should filter. Blank languages will be ignored.
     * @param languages the languages for which we should filter contents
     */
    public void setLanguages(String languages) {
        if (isNotBlank(languages)) {
            this.languages = languages;
        }
    }


    /**
     * @return the name for which we should filter contents
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name for which we should filter contents. Blank name will be ignored.
     * @param name the name for which we should filter contents
     */
    public void setName(String name) {
        if (isNotBlank(name)) {
            this.name = name;
        }
    }

    /**
     * @return the name of the field on which sorting should be performed
     */
    public String getSortColumn() {
        return sortColumn;
    }

    /**
     * Sets the name of the field on which sorting should be performed. Blank column name will be ignored.
     * @param sortColumn the name of the field on which sorting should be performed
     */
    public void setSortColumn(String sortColumn) {
        if (isNotBlank(sortColumn)) {
            this.sortColumn = sortColumn;
        }
    }

    /**
     * @return the sorting direction for these settings, either asc or desc
     */
    public String getSortDirection() {
        return sortDirection;
    }

    /**
     * Sets the sorting direction for these settings. Blank values will be ignored.
     * @param sortDirection the sorting direction for these settings, either asc or desc
     */
    public void setSortDirection(String sortDirection) {
        if (isNotBlank(sortDirection)) {
            this.sortDirection = sortDirection;
        }
    }

    /**
     * @return true if these settings accept (display) stream contents, false otherwise
     */
    public Boolean isStream() {
        return stream;
    }

    /**
     * Set whether these settings should accept (display) stream contents. Null will be ignored.
     * @param stream true if these settings accept (display) stream contents, false otherwise
     */
    public void setStream(Boolean stream) {
        if (stream != null) {
            this.stream = stream;
        }
    }

    /**
     * @return true if these settings accept (display) string contents, false otherwise
     */
    public Boolean isString() {
        return string;
    }

    /**
     * Set whether these settings should accept (display) string contents. Null will be ignored.
     * @param string true if these settings accept (display) string contents, false otherwise
     */
    public void setString(Boolean string) {
        if (string != null) {
            this.string = string;
        }
    }

    /**
     * @return the page to display, starting from 1
     */
    public Integer getPage() {
        return page;
    }

    /**
     * Sets the page to display. Null will be ignored.
     * @param page the page to display, starting from 1
     */
    public void setPage(Integer page) {
        if (page != null) {
            this.page = page;
        }
    }

    /**
     * @return the number of rows to display per page
     */
    public Integer getRows() {
        return rows;
    }

    /**
     * Sets the number of rows to display per page. Null will be ignored.
     * @param rows the number of rows to display per page
     */
    public void setRows(Integer rows) {
        if (rows != null) {
            this.rows = rows;
        }
    }

}
