package org.motechproject.rapidpro.service.impl;

import org.motechproject.rapidpro.domain.ContactMapping;
import org.motechproject.rapidpro.exception.NoMappingException;
import org.motechproject.rapidpro.repository.ContactMapperDataService;
import org.motechproject.rapidpro.service.ContactMapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Implementation of {@link ContactMapperServiceImpl}
 */
@Service
public class ContactMapperServiceImpl implements ContactMapperService {

    @Autowired
    private ContactMapperDataService contactMapperDataService;

    @Override
    public UUID getRapidproUUIDFromExternalId(String externalId) throws NoMappingException {
        return getMapping(externalId).getRapidproUUID();
    }

    @Override
    public void delete(String externalId) throws NoMappingException {
        ContactMapping mapping = getMapping(externalId);
        contactMapperDataService.delete(mapping);
    }

    @Override
    public void create(String externalId, UUID rapidproUUID) {
        contactMapperDataService.create(new ContactMapping(externalId, rapidproUUID));
    }

    @Override
    public boolean exists(String externalId) {
        try {
            getMapping(externalId);
            return true;
        } catch (NoMappingException e) {
            return false;
        }
    }

    private ContactMapping getMapping(String externalId) throws NoMappingException {
        ContactMapping mapping = contactMapperDataService.findByExternalId(externalId);
        if (mapping == null) {
            throw new NoMappingException(externalId);
        } else {
            return mapping;
        }
    }
}
