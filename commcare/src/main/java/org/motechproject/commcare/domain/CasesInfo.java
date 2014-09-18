package org.motechproject.commcare.domain;

import java.util.List;

/**
 * Wrapper for the List of CaseInfo that additionaly holds metadata of the cases
 */
public class CasesInfo {
    private List<CaseInfo> caseInfoList;
    private CommcareMetadataInfo metadataInfo;

    public CasesInfo(List<CaseInfo> caseInfoList, CommcareMetadataInfo metadataInfo) {
        this.caseInfoList = caseInfoList;
        this.metadataInfo = metadataInfo;
    }

    public List<CaseInfo> getCaseInfoList() {
        return caseInfoList;
    }

    public void setCaseInfoList(List<CaseInfo> caseInfoList) {
        this.caseInfoList = caseInfoList;
    }

    public CommcareMetadataInfo getMetadataInfo() {
        return metadataInfo;
    }

    public void setMetadataInfo(CommcareMetadataInfo metadataInfo) {
        this.metadataInfo = metadataInfo;
    }
}
