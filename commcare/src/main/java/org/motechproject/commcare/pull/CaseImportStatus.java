package org.motechproject.commcare.pull;

/**
 * Represents the status of an ongoing case import.
 */
public class CaseImportStatus {
    /**
     * The number of cases successfully imported.
     */
    private int casesImported;

    /**
     * The total number of cases to import.
     */
    private int totalCases;

    /**
     * The date of the last successfully imported case.
     */
    private String lastImportDate;

    /**
     * The ID of the last successfully imported case.
     */
    private String lastImportCaseId;

    /**
     * Whether the import ended in error.
     */
    private boolean error;

    /**
     * The error message, if applicable.
     */
    private String errorMsg;

    /**
     * Is import in progress currently.
     */
    private boolean importInProgress;

    /**
     * @return the number of cases successfully imported
     */
    public int getCasesImported() {
        return casesImported;
    }

    /**
     * @param casesImported the number of cases successfully imported
     */
    public void setCasesImported(int casesImported) {
        this.casesImported = casesImported;
    }

    /**
     * @return the total number of cases to import
     */
    public int getTotalCases() {
        return totalCases;
    }

    /**
     * @param totalCases the total number of cases to import
     */
    public void setTotalCases(int totalCases) {
        this.totalCases = totalCases;
    }

    /**
     * @return the date of the last successfully imported case
     */
    public String getLastImportDate() {
        return lastImportDate;
    }

    /**
     * @param lastImportDate the date of the last successfully imported case
     */
    public void setLastImportDate(String lastImportDate) {
        this.lastImportDate = lastImportDate;
    }

    /**
     * @return true if the import ended on error, false otherwise
     */
    public boolean isError() {
        return error;
    }

    /**
     * @param error true if the import ended on error, false otherwise
     */
    public void setError(boolean error) {
        this.error = error;
    }

    /**
     * @return the message for the error that stopped the import
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * @param errorMsg the message for the error that stopped the import
     */
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    /**
     * @return true if import is in progress, false otherwise
     */
    public boolean isImportInProgress() {
        return importInProgress;
    }

    /**
     * @param importInProgress true if import is in progress, false otherwise
     */
    public void setImportInProgress(boolean importInProgress) {
        this.importInProgress = importInProgress;
    }

    /**
     * @return the ID of the last successfully imported case
     */
    public String getLastImportCaseId() {
        return lastImportCaseId;
    }

    /**
     * @param lastImportCaseId the ID of the last successfully imported case
     */
    public void setLastImportCaseId(String lastImportCaseId) {
        this.lastImportCaseId = lastImportCaseId;
    }
}
