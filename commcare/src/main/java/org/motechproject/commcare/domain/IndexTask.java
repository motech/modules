package org.motechproject.commcare.domain;

import org.motechproject.commcare.request.IndexSubElement;

import java.util.ArrayList;
import java.util.List;

/**
 * A domain class to include in a case task in order to generate an index element with IndexSubElements as the indices.
 */
public class IndexTask {

    private List<IndexSubElement> indices;

    public IndexTask(List<IndexSubElement> indices) {
        if (indices == null) {
            this.indices = new ArrayList<>();
        } else {
            this.indices = indices;
        }
    }

    public List<IndexSubElement> getIndices() {
        return this.indices;
    }

    public void setIndices(List<IndexSubElement> indices) {
        this.indices = indices;
    }
}
