package org.motechproject.hub.service;

/**
 * This is an interface providing methods for fetching and distributing content
 * from an updated topic to all its subscribers.
 */
public interface ContentDistributionService {

    /**
     * Distributes the fetched content to all the subscribers subscribed to the
     * particular topic.
     *
     * @param url a <code>String</code> representing the updated topic URL
     */
    void distribute(String url);

}
