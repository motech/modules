package org.motechproject.batch.model;

/**
 * A representation of batch job statuses.
 */
public enum JobStatusLookup {

    ACTIVE(1),
    INACTIVE(2);

    private final int id;

    /**
     * @return the id of the status
     */
    public int getId() {
        return id;
    }

    JobStatusLookup(int id) {
        this.id = id;
    }

}
