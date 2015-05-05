package org.motechproject.dhis2.service.impl;

import org.motechproject.dhis2.domain.Stage;
import org.motechproject.dhis2.repository.StageDataService;
import org.motechproject.dhis2.rest.domain.ProgramStageDto;
import org.motechproject.dhis2.service.StageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Implementation of {@link org.motechproject.dhis2.service.StageService}
 */
@Service("stageService")
public class StageServiceImpl implements StageService {
    @Autowired
    private StageDataService stageDataService;

    @Override
    public List<Stage> findAll() {
        return stageDataService.retrieveAll();
    }

    @Override
    public Stage findById(String id) {
        return stageDataService.findByUuid(id);
    }

    @Override
    public Stage createFromDetails(ProgramStageDto details, String programId, boolean hasRegistration) {
        Stage stage = new Stage();
        stage.setUuid(details.getId());
        stage.setName(details.getName());
        stage.setRegistration(hasRegistration);
        stage.setProgram(programId);
        return stageDataService.create(stage);
    }

    @Override
    public void update(Stage stage) {
        stageDataService.update(stage);
    }

    @Override
    public void delete(Stage stage) {
        stageDataService.delete(stage);
    }

    @Override
    public void deleteAll() {
        stageDataService.deleteAll();
    }
}
