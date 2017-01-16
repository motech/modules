package org.motechproject.dhis2.service.impl;

import org.motechproject.dhis2.domain.DataElement;
import org.motechproject.dhis2.domain.DataSet;
import org.motechproject.dhis2.repository.DataSetDataService;
import org.motechproject.dhis2.rest.domain.DataElementDto;
import org.motechproject.dhis2.rest.domain.DataSetDto;
import org.motechproject.dhis2.service.DataElementService;
import org.motechproject.dhis2.service.DataSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the {@link DataSetService} interface.
 */
@Service("dataSetService")
public class DataSetServiceImpl implements DataSetService {

    @Autowired
    private DataSetDataService dataSetDataService;

    @Autowired
    private DataElementService dataElementService;


    @Override
    @Transactional
    public DataSet createFromDetails(DataSetDto dto) {
        DataSet dataSet = new DataSet();

        dataSet.setName(dto.getName());
        dataSet.setUuid(dto.getId());

        List<DataElement> dataElements = new ArrayList<>();
        for (DataElementDto dataElementDto : dto.getDataElements()) {
            dataElements.add(dataElementService.findById(dataElementDto.getId()));
        }
        dataSet.setDataElementList(dataElements);

        return dataSetDataService.create(dataSet);
    }

    @Override
    @Transactional
    public List<DataSet> findAll() {
        return dataSetDataService.retrieveAll();
    }

    @Override
    @Transactional
    public DataSet findByUuid(String uuid) {
        return dataSetDataService.findByUuid(uuid);
    }

    @Override
    @Transactional
    public void deleteAll() {
        dataSetDataService.deleteAll();
    }
}
