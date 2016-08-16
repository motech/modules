package org.motechproject.openmrs.domain;


import java.util.List;

/**
 * Represents a single CohortQuery Report containing report members
 */
public class CohortQueryReport {

    private String uuid;
    private List<CohortQueryReportMember> members;

    public CohortQueryReport() {}

    public class CohortQueryReportMember {

        private String uuid;
        private String display;

        public CohortQueryReportMember() {}

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getDisplay() {
            return display;
        }

        public void setDisplay(String display) {
            this.display = display;
        }
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<CohortQueryReportMember> getMembers() {
        return members;
    }

    public void setMembers(List<CohortQueryReportMember> members) {
        this.members = members;
    }
}
