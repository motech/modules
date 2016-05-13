package org.motechproject.rapidpro.service.impl;

import org.motechproject.rapidpro.domain.ContactMapping;
import org.motechproject.rapidpro.exception.NoMappingException;
import org.motechproject.rapidpro.repository.ContactMapperDataService;
import org.motechproject.rapidpro.service.ContactMapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link ContactMapperServiceImpl}
 */
@Service
public class ContactMapperServiceImpl implements ContactMapperService {

    @Autowired
    private ContactMapperDataService contactMapperDataService;

    @Override
    public String getRapidproUUIDFromExternalId(String externalId) throws NoMappingException {
        return getMapper(externalId).getRapidproUUID();
    }

    @Override
    public void delete(String externalId) throws NoMappingException {
        ContactMapping mapper = getMapper(externalId);
        contactMapperDataService.delete(mapper);
    }

    @Override
    public void create(String externalId, String rapidproUUID) {
        contactMapperDataService.create(new ContactMapping(externalId, rapidproUUID));
    }

    @Override
    public boolean exists(String externalId) {
        try {
            getMapper(externalId);
            return true;
        } catch (NoMappingException e) {
            return false;
        }
    }

    private ContactMapping getMapper(String externalId) throws NoMappingException {
        ContactMapping mapper = contactMapperDataService.findByExternalId(externalId);
        if (mapper == null) {
            throw new NoMappingException(externalId);
        } else {
            return mapper;
        }
    }
}
