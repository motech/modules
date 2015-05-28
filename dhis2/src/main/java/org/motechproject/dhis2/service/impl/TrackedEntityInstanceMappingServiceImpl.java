package org.motechproject.dhis2.service.impl;

import org.motechproject.admin.service.StatusMessageService;
import org.motechproject.dhis2.domain.TrackedEntityInstanceMapping;
import org.motechproject.dhis2.repository.TrackedEntityInstanceMappingDataService;
import org.motechproject.dhis2.service.TrackedEntityInstanceMappingService;
import org.motechproject.dhis2.service.TrackedEntityInstanceMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Implementation of {@link TrackedEntityInstanceMappingService}
 */
@Service("trackedEntityInstanceMapperService")
public class TrackedEntityInstanceMappingServiceImpl implements TrackedEntityInstanceMappingService {
    private static final String MODULE_NAME = "dhis2";

    @Autowired
    private StatusMessageService statusMessageService;

    @Autowired
    private TrackedEntityInstanceMappingDataService trackedEntityInstanceMappingDataService;

    @Override
    public TrackedEntityInstanceMapping create(String externalId, String dhisId) {
        TrackedEntityInstanceMapping mapper = new TrackedEntityInstanceMapping();
        mapper.setExternalName(externalId);
        mapper.setDhis2Uuid(dhisId);
        return trackedEntityInstanceMappingDataService.create(mapper);
    }

    @Override
    public String mapFromExternalId(String externalId) {
        TrackedEntityInstanceMapping mapper = trackedEntityInstanceMappingDataService.findByExternalName(externalId);

        if (mapper == null) {
            String msg = String.format("Failed to map for externalId: %s", externalId);
            statusMessageService.warn(msg, MODULE_NAME);
            throw new TrackedEntityInstanceMappingException("Failed to map for externalId: " + externalId);
        }

        return mapper.getDhis2Uuid();
    }

    @Override
    public void update(TrackedEntityInstanceMapping mapper) {
        trackedEntityInstanceMappingDataService.update(mapper);
    }

    @Override
    public void delete(TrackedEntityInstanceMapping mapper) {
        trackedEntityInstanceMappingDataService.delete(mapper);
    }

    @Override
    public List<TrackedEntityInstanceMapping> findAll() {
        return trackedEntityInstanceMappingDataService.retrieveAll();
    }

    @Override
    public TrackedEntityInstanceMapping findByExternalId(String externalId) {
        return trackedEntityInstanceMappingDataService.findByExternalName(externalId);
    }

    @Override
    public void deleteAll() {
        trackedEntityInstanceMappingDataService.deleteAll();
    }
}
