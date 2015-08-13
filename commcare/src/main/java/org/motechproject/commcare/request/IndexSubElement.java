package org.motechproject.commcare.request;

/**
 * Domain class that is converted to indices in the index element of the case XML.
 */
public class IndexSubElement {

    private String caseType;
    private String caseId;
    private String indexNodeName;

    /**
     * Creates a new indexSub XML with maximum of three sub-elements - caseId, caseType and indexNodeName. Values of
     * those elements will be equal to the given parameters. If any of the parameters is null the related element won't
     * be added.
     *
     * @param caseId  the value of the caseId element
     * @param caseType  the value of the caseType element
     * @param indexNodeName  the value of the indexNodeName element
     */
    public IndexSubElement(String caseId, String caseType, String indexNodeName) {
        this.caseId = caseId;
        this.caseType = caseType;
        this.indexNodeName = indexNodeName;
    }

    public String getCaseType() {
        return this.caseType;
    }

    public String getCaseId() {
        return this.caseId;
    }

    public String getIndexNodeName() {
        return this.indexNodeName;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public void setIndexNodeName(String indexNodeName) {
        this.indexNodeName = indexNodeName;
    }

}
