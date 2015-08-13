package org.motechproject.commcare.request;

/**
 * Represents a XML case request. Includes both metadata and case XML itself.
 */
public class CommcareRequestData {

    private String xmlns;
    private MetaElement meta;
    private CaseRequest ccCase;

    /**
     * Creates a commcare request with the xmlns attribute set to {@code xmlns}, the meta element set to {@code meta}
     * and the case element set to {@code ccCase}.
     *
     * @param xmlns  the value of the xmlns attribute, which should be the namespace of the XML element
     * @param meta  the value of the meta element, for more information about meta element, see {@see MetaElement}
     * @param ccCase  the value of the case element, for more information about case element, see {@see CaseRequest}
     */
    public CommcareRequestData(String xmlns, MetaElement meta,
            CaseRequest ccCase) {
        this.xmlns = xmlns;
        this.meta = meta;
        this.ccCase = ccCase;
    }

    public CaseRequest getCcCase() {
        return this.ccCase;
    }

    public MetaElement getMeta() {
        return this.meta;
    }

    public String getXmlns() {
        return this.xmlns;
    }
}
