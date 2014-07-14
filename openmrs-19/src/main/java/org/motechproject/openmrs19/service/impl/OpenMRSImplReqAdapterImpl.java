package org.motechproject.openmrs19.service.impl;

import org.motechproject.openmrs19.service.OpenMRSImplReqAdapter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("implReqAdapter")
public class OpenMRSImplReqAdapterImpl implements OpenMRSImplReqAdapter {

    public List<String> getRequired() {
        List<String> required = new ArrayList<>();

        required.add("motechId");
        required.add("firstName");
        required.add("lastName");
        required.add("gender");
        required.add("facilityId");

        return required;
    }

}
