package org.motechproject.commcare.request;

import org.motechproject.commcare.domain.IndexTask;
import org.motechproject.commcare.domain.UpdateTask;

/**
 * Represents a case XML request that can be sent to the CommCareHQ server.
 */
public class CaseRequest {

    private CreateElement createElement;
    private UpdateTask updateElement;
    private CloseElement closeElement;
    private IndexTask indexElement;
    private String userId;
    private String xmlns;
    private String dataXmlns;
    private String dateModified;
    private String caseId;

    /**
     * Creates a case request with the caseId attribute set to {@code caseId}, the userId attribute to {@code userId},
     * the dateModified attribute set to {@code dateModified} and the dataXmlns attribute set to {@code dataXmlns}.
     *
     * @param caseId  the value of the caseId attribute, which should be the ID of the case
     * @param userId  the value of the userId attribute, which should be the ID of the user
     * @param dateModified  the value of the dateModified attribute, which should be the last modification date
     * @param dataXmlns  the value of the dataXmlns attribute, which should be the namespace of the XML element
     */
    public CaseRequest(String caseId, String userId, String dateModified,
            String dataXmlns) {
        this.caseId = caseId;
        this.userId = userId;
        this.dateModified = dateModified;
        this.xmlns = "http://commcarehq.org/case/transaction/v2";
        this.dataXmlns = dataXmlns;
    }

    public String getDataXmlns() {
        return this.dataXmlns;
    }

    public void setDataXmlns(String dataXmlns) {
        this.dataXmlns = dataXmlns;
    }

    public void setIndexElement(IndexTask indexElement) {
        this.indexElement = indexElement;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public String getXmlns() {
        return this.xmlns;
    }

    public void setXmlns(String xmlns) {
        this.xmlns = xmlns;
    }

    public CaseRequest() {
        this.xmlns = "http://commcarehq.org/case/transaction/v2";
    }

    public String getDateModified() {
        return this.dateModified;
    }

    public String getCaseId() {
        return this.caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public CreateElement getCreateElement() {
        return this.createElement;
    }

    public void setCreateElement(CreateElement createElement) {
        this.createElement = createElement;
    }

    public UpdateTask getUpdateElement() {
        return this.updateElement;
    }

    public void setUpdateElement(UpdateTask updateElement) {
        this.updateElement = updateElement;
    }

    public IndexTask getIndexElement() {
        return this.indexElement;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return this.userId;
    }

    public CloseElement getCloseElement() {
        return this.closeElement;
    }

    public void setCloseElement(CloseElement closeElement) {
        this.closeElement = closeElement;
    }
}
