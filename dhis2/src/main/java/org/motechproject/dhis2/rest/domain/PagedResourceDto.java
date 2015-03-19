package org.motechproject.dhis2.rest.domain;

import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.List;

public class PagedResourceDto<T extends BaseDto> {
    private PagerDto pager;
    private List<T> resources;

    @JsonAnySetter
    public void setResources(String key, List<T> resources) {
        this.resources = resources;
    }

    public PagerDto getPager() {
        return pager;
    }

    public void setPager(PagerDto pager) {
        this.pager = pager;
    }

    public List<T> getResources() {
        return resources;
    }
}
