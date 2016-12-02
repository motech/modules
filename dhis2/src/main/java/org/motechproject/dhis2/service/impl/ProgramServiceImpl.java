package org.motechproject.dhis2.service.impl;

import org.motechproject.dhis2.domain.Program;
import org.motechproject.dhis2.domain.Stage;
import org.motechproject.dhis2.domain.TrackedEntity;
import org.motechproject.dhis2.domain.TrackedEntityAttribute;
import org.motechproject.dhis2.repository.ProgramDataService;
import org.motechproject.dhis2.rest.domain.ProgramDto;
import org.motechproject.dhis2.service.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Implementation of {@link org.motechproject.dhis2.service.ProgramService}
 */
@Service("programService")
public class ProgramServiceImpl implements ProgramService {
    @Autowired
    private ProgramDataService programDataService;

    @Override
    @Transactional
    public Program findById(String id) {
        return programDataService.findByUuid(id);
    }

    @Override
    @Transactional
    public void update(Program program) {
        programDataService.update(program);
    }

    @Override
    @Transactional
    public void delete(Program program) {
        programDataService.delete(program);
    }

    @Override
    @Transactional
    public Program createFromDetails(ProgramDto details, TrackedEntity trackedEntity, List<Stage> stages, List<TrackedEntityAttribute> trackedEntityAttributes) {
        Program program = new Program();
        program.setUuid(details.getId());
        program.setName(details.getName());
        program.setRegistration(details.getRegistration());
        program.setSingleEvent(details.getSingleEvent());
        program.setProgramType(details.getProgramType());
        program.setTrackedEntity(trackedEntity);
        program.setStages(stages);
        program.setAttributes(trackedEntityAttributes);

        return programDataService.create(program);
    }

    @Override
    @Transactional
    public void deleteAll() {
        programDataService.deleteAll();
    }

    @Override
    @Transactional
    public List<Program> findAll() {
        return programDataService.retrieveAll();
    }

    @Override
    @Transactional
    public List<Program> findByRegistration(boolean registration) {
        return programDataService.findByRegistration(registration);
    }
}
