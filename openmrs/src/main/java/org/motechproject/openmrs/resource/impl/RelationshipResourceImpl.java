package org.motechproject.openmrs.resource.impl;

import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.domain.RelationshipListResult;
import org.motechproject.openmrs.resource.RelationshipResource;
import org.motechproject.openmrs.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

@Service
public class RelationshipResourceImpl extends BaseResource implements RelationshipResource {

    @Autowired
    public RelationshipResourceImpl(RestOperations restOperations) {
        super(restOperations);
    }

    @Override
    public RelationshipListResult getByPersonUuid(Config config, String personUuid) {
        String responseJson = getJson(config, "/relationship?person={personUuid}&v=full", personUuid);
        return (RelationshipListResult) JsonUtils.readJson(responseJson, RelationshipListResult.class);
    }
}
