package org.motechproject.openlmis.service.impl;

import java.util.List;

import org.motechproject.openlmis.domain.Program;
import org.motechproject.openlmis.repository.ProgramDataService;
import org.motechproject.openlmis.rest.domain.ProgramDto;
import org.motechproject.openlmis.service.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Implementation of {@link org.motechproject.openlmis.service.ProgramService}
 */
@Service("programService")
public class ProgramServiceImpl implements ProgramService {
    @Autowired
    private ProgramDataService programDataService;

    @Override
    public Program findByOpenlmisId(Integer id) {
        return programDataService.findByOpenlmisId(id);
    }
    
    @Override
    public List<Program> findByName(String name) {
        return programDataService.findByName(name);
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
        program.setOpenlmisid(details.getId());
        program.setName(details.getName());
        program.setDescription(details.getDescription());
        program.setCode(details.getCode());
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
    public Program findByCode(String code) {
        return programDataService.findByCode(code);
    }

    

}
