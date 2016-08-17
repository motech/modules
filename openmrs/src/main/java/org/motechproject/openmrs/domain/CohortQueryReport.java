package org.motechproject.openmrs.domain;


import java.util.List;
import java.util.Objects;

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

        @Override
        public int hashCode() {
            return Objects.hash(uuid, display);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }

            CohortQueryReportMember other = (CohortQueryReportMember) obj;

            return Objects.equals(this.uuid, other.uuid) && Objects.equals(this.display, other.display);
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

    @Override
    public int hashCode() {
        return Objects.hash(uuid, members);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        CohortQueryReport other = (CohortQueryReport) obj;

        return Objects.equals(this.uuid, other.uuid) && Objects.equals(this.members, other.members);
    }
}
