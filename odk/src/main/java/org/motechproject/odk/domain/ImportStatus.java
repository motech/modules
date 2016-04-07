package org.motechproject.odk.domain;

/**
 * Represents the status of a form definition import.
 */
public class ImportStatus {

    private boolean imported;

    public ImportStatus(boolean imported) {
        this.imported = imported;
    }

    public boolean isImported() {
        return imported;
    }

    public void setImported(boolean imported) {
        this.imported = imported;
    }
}
