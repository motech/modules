package org.motechproject.dhis2.service;

/**
 * Used to synchronize this module with the DHIS2 server schema
 */
public interface SyncService {

    /**
     * Queries the DHIS2 server to get the current schema and then persists that
     * information in MDS. If the process fails, all of the records pertaining to the DHIS2 schema
     * are deleted.
     * @return true if successful; false otherwise
     */
    boolean sync();
}
