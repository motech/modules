package org.motechproject.dhis2.service;

import org.motechproject.dhis2.domain.Stage;
import org.motechproject.dhis2.rest.domain.ProgramStageDto;

/**
 * Manages CRUD operations for a {@link org.motechproject.dhis2.domain.Stage}
 */
public interface StageService extends GenericCrudService<Stage> {
    Stage findById(String id);
    Stage createFromDetails(ProgramStageDto details, String programId, boolean hasRegistration);
}
