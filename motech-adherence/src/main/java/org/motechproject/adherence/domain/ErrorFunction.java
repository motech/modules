package org.motechproject.adherence.domain;

public class ErrorFunction {
    protected final int dosesTaken;
    protected final int totalDoses;

    public ErrorFunction(int dosesTaken, int totalDoses) {
        this.dosesTaken = dosesTaken;
        this.totalDoses = totalDoses;
    }

    public int getDosesTaken() {
        return dosesTaken;
    }

    public int getTotalDoses() {
        return totalDoses;
    }
}
