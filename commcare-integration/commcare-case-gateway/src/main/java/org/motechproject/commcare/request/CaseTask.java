package org.motechproject.commcare.request;

/**
 * Created by IntelliJ IDEA.
 * User: pchandra
 * Date: 4/2/12
 * Time: 4:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class CaseTask {

    private String caseType = "Task";
    private String caseName;
    private String ownerId;
    private String caseId;
    private String userId;
    private String dateModified;
    private String taskId;
    private String dateEligible;

    public Pregnancy getPregnancy() {
        return pregnancy;
    }

    public void setPregnancy(Pregnancy pregnancy) {
        this.pregnancy = pregnancy;
    }

    private Pregnancy pregnancy;

    public String getCaseType() {
        return caseType;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getDateEligible() {
        return dateEligible;
    }

    public void setDateEligible(String dateEligible) {
        this.dateEligible = dateEligible;
    }

    public String getDateExpires() {
        return dateExpires;
    }

    public void setDateExpires(String dateExpires) {
        this.dateExpires = dateExpires;
    }

    private String dateExpires;
}
