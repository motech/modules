package org.motechproject.commcare.domain;

import org.motechproject.commcare.request.Pregnancy;

public class CaseTask {

    private String caseType = "task";
    private String caseName;
    private String ownerId;
    private String caseId;
    private String userId;
    private String currentTime;
    private String taskId;
    private String dateEligible;
    private String dateExpires;

    private Pregnancy pregnancy;

    public Pregnancy getPregnancy() {
        return pregnancy;
    }

    public void setPregnancy(Pregnancy pregnancy) {
        this.pregnancy = pregnancy;
    }

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

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CaseTask caseTask = (CaseTask) o;

        if (caseId != null ? !caseId.equals(caseTask.caseId) : caseTask.caseId != null) return false;
        if (caseName != null ? !caseName.equals(caseTask.caseName) : caseTask.caseName != null) return false;
        if (caseType != null ? !caseType.equals(caseTask.caseType) : caseTask.caseType != null) return false;
        if (dateEligible != null ? !dateEligible.equals(caseTask.dateEligible) : caseTask.dateEligible != null)
            return false;
        if (dateExpires != null ? !dateExpires.equals(caseTask.dateExpires) : caseTask.dateExpires != null)
            return false;
        if (currentTime != null ? !currentTime.equals(caseTask.currentTime) : caseTask.currentTime != null)
            return false;
        if (ownerId != null ? !ownerId.equals(caseTask.ownerId) : caseTask.ownerId != null) return false;
        if (pregnancy != null ? !pregnancy.equals(caseTask.pregnancy) : caseTask.pregnancy != null) return false;
        if (taskId != null ? !taskId.equals(caseTask.taskId) : caseTask.taskId != null) return false;
        if (userId != null ? !userId.equals(caseTask.userId) : caseTask.userId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = caseType != null ? caseType.hashCode() : 0;
        result = 31 * result + (caseName != null ? caseName.hashCode() : 0);
        result = 31 * result + (ownerId != null ? ownerId.hashCode() : 0);
        result = 31 * result + (caseId != null ? caseId.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (currentTime != null ? currentTime.hashCode() : 0);
        result = 31 * result + (taskId != null ? taskId.hashCode() : 0);
        result = 31 * result + (dateEligible != null ? dateEligible.hashCode() : 0);
        result = 31 * result + (dateExpires != null ? dateExpires.hashCode() : 0);
        result = 31 * result + (pregnancy != null ? pregnancy.hashCode() : 0);
        return result;
    }
}
