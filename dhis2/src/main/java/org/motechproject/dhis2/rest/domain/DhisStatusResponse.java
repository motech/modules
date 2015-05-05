package org.motechproject.dhis2.rest.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

/**
 * A class to model DHIS2 responses that result from creating entities.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DhisStatusResponse {
    public static enum DhisStatus {
        SUCCESS,
        ERROR
    }

    private DhisStatus status;
    private ImportCount importCount;
    private String reference;

    public DhisStatus getStatus() {
        return status;
    }

    public void setStatus(DhisStatus status) {
        this.status = status;
    }

    public ImportCount getImportCount() {
        return importCount;
    }

    public void setImportCount(ImportCount importCount) {
        this.importCount = importCount;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, importCount, reference);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final DhisStatusResponse other = (DhisStatusResponse) obj;
        return Objects.equals(this.status, other.status)
                && Objects.equals(this.importCount, other.importCount)
                && Objects.equals(this.reference, other.reference);
    }

    public static class ImportCount {
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
            final ImportCount other = (ImportCount) obj;
            return Objects.equals(this.imported, other.imported)
                    && Objects.equals(this.updated, other.updated)
                    && Objects.equals(this.ignored, other.ignored)
                    && Objects.equals(this.deleted, other.deleted);
        }
    }


}
