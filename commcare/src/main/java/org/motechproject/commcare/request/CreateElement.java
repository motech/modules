package org.motechproject.commcare.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Represents a create element in an XML document.
 */
@XStreamAlias("create")
public class CreateElement {

    private String caseType;
    private String caseName;
    private String ownerId;

    /**
     * Creates a new create XML element with maximum of three sub-elements - caseType, caseName and ownerId. Values of
     * those elements will be equal to the given parameters. If any of the parameters is null the related element won't
     * be added.
     *
     * @param caseType  the value of the caseType element, which should contain the type of the case
     * @param caseName  the value of the caseName element, which should contain the name of the case
     * @param ownerId  the value of the ownerId element, which should contain the ID of the owner
     */
    public CreateElement(String caseType, String caseName, String ownerId) {
        this.caseType = caseType;
        this.caseName = caseName;
        this.ownerId = ownerId;
    }

    public String getCaseType() {
        return this.caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public String getCaseName() {
        return this.caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
}
