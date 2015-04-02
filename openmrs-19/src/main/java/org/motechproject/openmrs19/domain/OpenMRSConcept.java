package org.motechproject.openmrs19.domain;

import java.util.List;

/**
 * Maintains observation types
 */
public class OpenMRSConcept {
    private OpenMRSConceptName name;
    private String id;
    private String uuid;
    private String dataType;
    private String conceptClass;
    private String display;
    private List<OpenMRSConceptName> names;

    public OpenMRSConcept(OpenMRSConceptName name) {
        this.name = name;
    }

    public OpenMRSConcept() {
        this(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OpenMRSConcept)) {
            return false;
        }
        OpenMRSConcept that = (OpenMRSConcept) o;
        if (name != null ? !name.equals(that.name) : that.name != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OpenMRSConceptName getName() {
        return name;
    }

    public void setName(OpenMRSConceptName name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getConceptClass() {
        return conceptClass;
    }

    public void setConceptClass(String conceptClass) {
        this.conceptClass = conceptClass;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public List<OpenMRSConceptName> getNames() {
        return names;
    }

    public void setNames(List<OpenMRSConceptName> names) {
        this.names = names;
    }
}
