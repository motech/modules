package org.motechproject.dhis2.service.impl;

import org.motechproject.dhis2.domain.TrackedEntityAttribute;
import org.motechproject.dhis2.repository.TrackedEntityAttributeDataService;
import org.motechproject.dhis2.rest.domain.TrackedEntityAttributeDto;
import org.motechproject.dhis2.service.TrackedEntityAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Implementation of {@link org.motechproject.dhis2.service.TrackedEntityAttributeService}
 */
@Service("trackedEntityAttributeService")
public class TrackedEntityAttributeServiceImpl implements TrackedEntityAttributeService {
    @Autowired
    private TrackedEntityAttributeDataService trackedEntityAttributeDataService;

    @Override
    @Transactional
    public List<TrackedEntityAttribute> findAll() {
        return trackedEntityAttributeDataService.retrieveAll();
    }

    @Override
    @Transactional
    public TrackedEntityAttribute findById(String id) {
        return trackedEntityAttributeDataService.findByUuid(id);
    }

    @Override
    @Transactional
    public TrackedEntityAttribute createFromDetails(TrackedEntityAttributeDto details) {
        TrackedEntityAttribute trackedEntityAttribute = new TrackedEntityAttribute();
        trackedEntityAttribute.setUuid(details.getId());
        trackedEntityAttribute.setName(details.getName());
        return trackedEntityAttributeDataService.create(trackedEntityAttribute);
    }

    @Override
    @Transactional
    public void update(TrackedEntityAttribute trackedEntityAttribute) {
        trackedEntityAttributeDataService.update(trackedEntityAttribute);
    }

    @Override
    @Transactional
    public void delete(TrackedEntityAttribute trackedEntityAttribute) {
        trackedEntityAttributeDataService.delete(trackedEntityAttribute);
    }

    @Override
    @Transactional
    public void deleteAll() {
        trackedEntityAttributeDataService.deleteAll();
    }
}
