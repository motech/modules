package org.motechproject.commcare.pull;

/**
 * Represents the status of an ongoing form import.
 */
public class FormImportStatus {

    /**
     * The number of forms successfully imported.
     */
    private int formsImported;

    /**
     * The total number of forms to import.
     */
    private int totalForms;

    /**
     * The date of the last successfully imported form.
     */
    private String lastImportDate;

    /**
     * Whether the import ended in error.
     */
    private boolean error;

    /**
     * The error message, if applicable.
     */
    private String errorMsg;

    /**
     * @return the number of forms successfully imported
     */
    public int getFormsImported() {
        return formsImported;
    }

    /**
     * @param formsImported the number of forms successfully imported
     */
    public void setFormsImported(int formsImported) {
        this.formsImported = formsImported;
    }

    /**
     * @return the total number of forms to import
     */
    public int getTotalForms() {
        return totalForms;
    }

    /**
     * @param totalForms the total number of forms to import
     */
    public void setTotalForms(int totalForms) {
        this.totalForms = totalForms;
    }

    /**
     * @return the date of the last successfully imported form
     */
    public String getLastImportDate() {
        return lastImportDate;
    }

    /**
     * @param lastImportDate the date of the last successfully imported form
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
}
