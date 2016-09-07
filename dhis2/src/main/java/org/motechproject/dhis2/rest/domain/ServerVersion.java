package org.motechproject.dhis2.rest.domain;

/**
 * Representation of the DHIS server version. Also allows easy comparison of the versions.
 */
public class ServerVersion {

    public static final String UNKNOWN = "unknown";
    public static final String V2_18 = "2.18";
    public static final String V2_19 = "2.19";
    public static final String V2_21 = "2.21";
    public static final String V2_22 = "2.22";

    private String version;

    public ServerVersion(String version) {
        this.version = version;
    }

    public boolean isAfter(String versionToCompare) {
        return !version.equals(UNKNOWN) && version.compareTo(versionToCompare) > 0;
    }

    public boolean isSameOrAfter(String versionToCompare) {
        return !version.equals(UNKNOWN) && version.compareTo(versionToCompare) >= 0;
    }

    public boolean isSame(String versionToCompare) {
        return !version.equals(UNKNOWN) && version.equals(versionToCompare);
    }

    public boolean isBefore(String versionToCompare) {
        return !version.equals(UNKNOWN) && version.compareTo(versionToCompare) < 0;
    }

    public boolean isSameOrBefore(String versionToCompare) {
        return !version.equals(UNKNOWN) && version.compareTo(versionToCompare) <= 0;
    }

}
