package org.motechproject.openlmis.rest.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

public class ResourceListDto<T> {
    
    private List<T> resources;

    @JsonAnySetter
    public void setResources(String name, List<T> resources) {
        this.resources = resources;
    }

    @JsonAnyGetter
    public List<T> getResources() {
        return resources;
    }
}
