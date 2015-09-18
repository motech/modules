package org.motechproject.hub.model;

/**
 * The enum class defining valid codes for content distribution status.
 */
public enum DistributionStatusLookup {

    /**
     * Successfully distributed content
     */
    SUCCESS(1, "success"),
    /**
     * Content distribution failed
     */
    FAILURE(2, "failure");

    private final String status;
    private final int id;

    /**
     * Gets the id of distribution status.
     *
     * @return the id of distribution status
     */
    public int getId() {
        return id;
    }

    private DistributionStatusLookup(int id, String status) {
        this.status = status;
        this.id = id;
    }

    @Override
    public String toString() {
        return this.status;
    }

}
