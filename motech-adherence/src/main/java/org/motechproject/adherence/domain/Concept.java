package org.motechproject.adherence.domain;

public class Concept {

    private String referenceId;
    private String tokenId;

    private Concept() {
    }

    public Concept(String referenceId, String tokenId) {
        this.referenceId = referenceId;
        this.tokenId = tokenId;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public String getTokenId() {
        return tokenId;
    }

    private void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    private void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }
}
