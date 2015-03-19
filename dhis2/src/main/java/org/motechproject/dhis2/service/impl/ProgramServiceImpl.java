package org.motechproject.dhis2.service.impl;

import org.motechproject.dhis2.domain.Program;
import org.motechproject.dhis2.repository.ProgramDataService;
import org.motechproject.dhis2.rest.domain.ProgramDto;
import org.motechproject.dhis2.service.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Implementation of {@link org.motechproject.dhis2.service.ProgramService}
 */
@Service("programService")
public class ProgramServiceImpl implements ProgramService {
    @Autowired
    private ProgramDataService programDataService;

    @Override
    public Program findById(String id) {
        return programDataService.findByUuid(id);
    }

    @Override
    public void update(Program program) {
        programDataService.update(program);
    }

    @Override
    public void delete(Program program) {
        programDataService.delete(program);
    }

    @Override
    public Program createFromDetails(ProgramDto details) {
        Program program = new Program();
        program.setUuid(details.getId());
        program.setName(details.getName());
        program.setRegistration(details.getRegistration());
        program.setSingleEvent(details.getSingleEvent());
        return programDataService.create(program);
    }

    @Override
    public void deleteAll() {
        programDataService.deleteAll();
    }

    @Override
    public List<Program> findAll() {
        return programDataService.retrieveAll();
    }

    @Override
    public List<Program> findByRegistration(boolean registration) {
        return programDataService.findByRegistration(registration);
    }
}
