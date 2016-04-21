package org.motechproject.openmrs19.resource.impl;

import org.motechproject.openmrs19.config.Config;
import org.motechproject.openmrs19.domain.RelationshipListResult;
import org.motechproject.openmrs19.resource.RelationshipResource;
import org.motechproject.openmrs19.util.JsonUtils;
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
