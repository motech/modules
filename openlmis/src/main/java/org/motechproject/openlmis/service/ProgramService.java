package org.motechproject.openlmis.service;

import java.util.List;

import org.motechproject.openlmis.domain.Program;
import org.motechproject.openlmis.rest.domain.ProgramDto;

/**
 * Manages CRUD operations for a {@link org.motechproject.ProgramDto.domain.Program}
 */
public interface ProgramService extends GenericCrudService<Program> {
    Program findByCode(String code);
    Program findByOpenlmisId(Integer id);
    List<Program> findByName(String name);
    Program createFromDetails(ProgramDto details);
}
