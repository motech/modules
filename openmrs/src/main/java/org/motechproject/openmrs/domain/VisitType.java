package org.motechproject.openmrs.domain;


import java.util.Objects;

/**
 * Class representing a type of the visit.
 */
public class VisitType {

    private String uuid;
    private String display;

    public VisitType(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, display);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof VisitType)) {
            return false;
        }

        VisitType other = (VisitType) o;

        return Objects.equals(this.uuid, other.uuid) && Objects.equals(this.display, other.display);
    }
}
