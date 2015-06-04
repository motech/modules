package org.motechproject.dhis2.rest.domain;

import java.util.Objects;

/**
 * A class to model a DHIS2 api response import count
 */
public class ImportCountDto {
    private int imported;
    private int updated;
    private int ignored;
    private int deleted;

    public int getImported() {
        return imported;
    }

    public void setImported(int imported) {
        this.imported = imported;
    }

    public int getUpdated() {
        return updated;
    }

    public void setUpdated(int updated) {
        this.updated = updated;
    }

    public int getIgnored() {
        return ignored;
    }

    public void setIgnored(int ignored) {
        this.ignored = ignored;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    @Override
    public int hashCode() {
        return Objects.hash(imported, updated, ignored, deleted);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ImportCountDto other = (ImportCountDto) obj;
        return Objects.equals(this.imported, other.imported)
                && Objects.equals(this.updated, other.updated)
                && Objects.equals(this.ignored, other.ignored)
                && Objects.equals(this.deleted, other.deleted);
    }
}
