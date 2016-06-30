package org.motechproject.rapidpro.webservice.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Representation of a paginated response from Rapidpro.
 *
 * @param <T> The type of the objects returned in the results collection field.
 */
public class PaginatedResponse<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private String count;
    private String next;
    private String previous;
    private List<T> results;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "PagedResponse{" +
                "count='" + count + '\'' +
                ", next='" + next + '\'' +
                ", previous='" + previous + '\'' +
                ", results=" + results +
                '}';
    }
}
