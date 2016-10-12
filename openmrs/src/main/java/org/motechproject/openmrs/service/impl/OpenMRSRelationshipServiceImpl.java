package org.motechproject.openmrs.service.impl;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import org.apache.commons.lang.StringUtils;
import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.domain.Relationship;
import org.motechproject.openmrs.domain.RelationshipListResult;
import org.motechproject.openmrs.exception.OpenMRSException;
import org.motechproject.openmrs.resource.RelationshipResource;
import org.motechproject.openmrs.service.OpenMRSConfigService;
import org.motechproject.openmrs.service.OpenMRSRelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;

@Service("relationshipService")
public class OpenMRSRelationshipServiceImpl implements OpenMRSRelationshipService {

    @Autowired
    private OpenMRSConfigService configService;

    @Autowired
    private RelationshipResource relationshipResource;

    @Override
    public List<Relationship> getByTypeUuidAndPersonUuid(String configName, String typeUuid, String personUuid) {
        Config config = configService.getConfigByName(configName);

        try {
            RelationshipListResult result = relationshipResource.getByPersonUuid(config, personUuid);

            //temporary workaround until OpenMRS server-side filtering for the relationships is fixed
            return new ArrayList<>(Collections2.filter(result.getResults(), new Predicate<Relationship>() {
                @Override
                public boolean apply(Relationship relationship) {
                    String personBUuid = null;
                    String relationshipTypeUuid = null;

                    if (relationship.getPersonB() != null) {
                        personBUuid = relationship.getPersonB().getUuid();
                    }

                    if (relationship.getRelationshipType() != null) {
                        relationshipTypeUuid = relationship.getRelationshipType().getUuid();
                    }

                    return StringUtils.equals(personBUuid, personUuid)
                            && StringUtils.equals(relationshipTypeUuid, typeUuid);
                }
            }));
        } catch (HttpClientErrorException e) {
            throw new OpenMRSException(String.format("Could not get Relationship for Person uuid: %s. %s %s", personUuid,
                    e.getMessage(), e.getResponseBodyAsString()), e);
        }
    }
}
