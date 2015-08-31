package org.motechproject.commcare.domain;

import java.util.List;

/**
 * Represents a list of stock transactions with metadata, as returned by Commcare.
 */
public class CommcareStockTransactionList {

    /**
     * The metadata object.
     */
    private CommcareMetadataJson meta;

    /**
     * The actual stock transactions.
     */
    private List<CommcareStockTransaction> objects;

    /**
     * @return the metadata associated with this list
     */
    public CommcareMetadataJson getMeta() {
        return meta;
    }

    /**
     * @param meta the metadata associated with this list
     */
    public void setMeta(CommcareMetadataJson meta) {
        this.meta = meta;
    }

    /**
     * @return the list of stock transactions
     */
    public List<CommcareStockTransaction> getObjects() {
        return objects;
    }

    /**
     * @param objects the list of stock transactions
     */
    public void setObjects(List<CommcareStockTransaction> objects) {
        this.objects = objects;
    }

}
