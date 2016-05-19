package org.motechproject.openlmis.service;

/**
 * Used to synchronize this module with the Open LMIS server schema
 */
public interface SyncService {

    /**
     * Queries the Open LMIS server to get the current schema and then persists that
     * information in MDS. If the process fails, all of the records pertaining to the Open LMIS schema
     * are deleted.
     * @return true if successful; false otherwise
     */
    boolean sync();
}
