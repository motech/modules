package org.motechproject.openmrs19.domain;

import java.util.Objects;

public class OpenMRSConceptName {
    private String name;
    private String locale = "en";
    private String conceptNameType = "FULLY_SPECIFIED";

    public OpenMRSConceptName(String name) {
        this.name = name;
    }

    public OpenMRSConceptName(String name, String locale, String conceptNameType) {
        this.name = name;
        this.locale = locale;
        this.conceptNameType = conceptNameType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getConceptNameType() {
        return conceptNameType;
    }

    public void setConceptNameType(String conceptNameType) {
        this.conceptNameType = conceptNameType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OpenMRSConceptName)) {
            return false;
        }

        OpenMRSConceptName conceptName = (OpenMRSConceptName) o;

        return Objects.equals(name, conceptName.name) && Objects.equals(locale, conceptName.locale) &&
                Objects.equals(conceptNameType, conceptName.conceptNameType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, locale, conceptNameType);
    }
}
