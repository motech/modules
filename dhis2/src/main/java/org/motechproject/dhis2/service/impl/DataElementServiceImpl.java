package org.motechproject.dhis2.service.impl;

import org.motechproject.dhis2.domain.DataElement;
import org.motechproject.dhis2.repository.DataElementDataService;
import org.motechproject.dhis2.rest.domain.DataElementDto;
import org.motechproject.dhis2.service.DataElementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link org.motechproject.dhis2.service.DataElementService}
 */
@Service("dataElementService")
public class DataElementServiceImpl implements DataElementService {
    @Autowired
    private DataElementDataService dataElementDataService;

    @Override
    @Transactional
    public List<DataElement> findAll() {
        return dataElementDataService.retrieveAll();
    }

    @Override
    @Transactional
    public DataElement findById(String id) {
        return dataElementDataService.findByUuid(id);
    }

    @Override
    @Transactional
    public DataElement createFromDetails(DataElementDto details) {
        DataElement dataElement = new DataElement();
        dataElement.setUuid(details.getId());
        dataElement.setName(details.getName());
        return dataElementDataService.create(dataElement);
    }

    @Override
    @Transactional
    public void update(DataElement dataElement) {
        dataElementDataService.update(dataElement);
    }

    @Override
    @Transactional
    public void delete(DataElement dataElement) {
        dataElementDataService.delete(dataElement);
    }

    @Override
    @Transactional
    public void deleteAll() {
        dataElementDataService.deleteAll();
    }

    @Override
    @Transactional
    public DataElement findByName(String name) {
        return dataElementDataService.findByName(name);
    }
}
