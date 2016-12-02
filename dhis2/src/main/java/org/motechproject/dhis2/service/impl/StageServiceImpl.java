package org.motechproject.dhis2.service.impl;

import org.motechproject.dhis2.domain.DataElement;
import org.motechproject.dhis2.domain.Stage;
import org.motechproject.dhis2.repository.StageDataService;
import org.motechproject.dhis2.rest.domain.ProgramStageDto;
import org.motechproject.dhis2.service.StageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Implementation of {@link org.motechproject.dhis2.service.StageService}
 */
@Service("stageService")
public class StageServiceImpl implements StageService {
    @Autowired
    private StageDataService stageDataService;

    @Override
    @Transactional
    public List<Stage> findAll() {
        return stageDataService.retrieveAll();
    }

    @Override
    @Transactional
    public Stage findById(String id) {
        return stageDataService.findByUuid(id);
    }

    @Override
    @Transactional
    public Stage createFromDetails(ProgramStageDto details, String programId, boolean hasRegistration, List<DataElement> dataElements) {
        Stage stage = new Stage();
        stage.setUuid(details.getId());
        stage.setName(details.getName());
        stage.setRegistration(hasRegistration);
        stage.setProgram(programId);
        stage.setDataElements(dataElements);
        return stageDataService.create(stage);
    }

    @Override
    @Transactional
    public void update(Stage stage) {
        stageDataService.update(stage);
    }

    @Override
    @Transactional
    public void delete(Stage stage) {
        stageDataService.delete(stage);
    }

    @Override
    @Transactional
    public void deleteAll() {
        stageDataService.deleteAll();
    }
}
