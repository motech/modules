package org.motechproject.dhis2.event;

import org.motechproject.dhis2.service.DhisService;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Event Handler for the DHIS2 module. The public methods listen for the event subjects listed in
 * {@link org.motechproject.dhis2.event.EventSubjects}
 */
@Service
public class EventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventHandler.class);

    @Autowired
    private DhisService dhisService;

    /**
     * Parses the MotechEvent and creates a {@link org.motechproject.dhis2.rest.domain.TrackedEntityInstanceDto}
     * which is then sent to the DHIS2 server via {@link org.motechproject.dhis2.rest.service.DhisWebService}
     *
     * @param event MotechEvent pertaining to tracked entity instance creation.
     */
    @MotechListener(subjects = EventSubjects.CREATE_ENTITY)
    public void handleCreate (MotechEvent event) {
        LOGGER.debug("Handling CREATE_ENTITY MotechEvent");
        dhisService.createEntity(event.getParameters());
    }

    /**
     * Parses the MotechEvent and creates a {@link org.motechproject.dhis2.rest.domain.EnrollmentDto}
     * which is then sent to the DHIS2 server via {@link org.motechproject.dhis2.rest.service.DhisWebService}
     *
     * @param event MotechEvent pertaining to enrolling a tracked entity instance in a program.
     */
    @MotechListener(subjects = {EventSubjects.ENROLL_IN_PROGRAM})
    public void handleEnrollment (MotechEvent event) {
        dhisService.enrollInProgram(event.getParameters());
    }


    /**
     * Parses the MotechEvent and creates a {@link org.motechproject.dhis2.rest.domain.DhisEventDto}
     * which is then sent to the DHIS2 server via {@link org.motechproject.dhis2.rest.service.DhisWebService}
     *
     * @param event MotechEvent pertaining to a DHIS2 program stage event.
     */
    @MotechListener(subjects = {EventSubjects.UPDATE_PROGRAM_STAGE})
    public void handleStageUpdate (MotechEvent event) {
        dhisService.updateProgramStage(event.getParameters());
    }

    /**
     * Parses the event and creates a {@link org.motechproject.dhis2.rest.domain.TrackedEntityInstanceDto}
     * and a {@link org.motechproject.dhis2.rest.domain.EnrollmentDto} which is then sent to the DHIS2 server
     * via {@link org.motechproject.dhis2.rest.service.DhisWebService}
     *
     * @param event pertaining to combined DHIS2 tracked entity instance creation and enrollment.
     */
    @MotechListener(subjects = {EventSubjects.CREATE_AND_ENROLL})
    public void handleCreateAndEnroll (MotechEvent event) {
        dhisService.createAndEnroll(event.getParameters());
    }

    /**
     * Parses the event and creates a {@link org.motechproject.dhis2.rest.domain.DataValueDto} which
     * is then sent to the DHIS2 server via {@link org.motechproject.dhis2.rest.service.DhisWebService}
     *
     * @param event
     */
    @MotechListener(subjects = EventSubjects.SEND_DATA_VALUE)
    public void handleDataValue(MotechEvent event) {
        dhisService.sendDataValue(event.getParameters());
    }

    /**
     * Parses the event and creates a{@link org.motechproject.dhis2.rest.domain.DataValueSetDto}which
     * is then sent to the DHIS2 server via {@link org.motechproject.dhis2.rest.service.DhisWebService}
     *
     * @param event
     */
    @MotechListener(subjects = EventSubjects.SEND_DATA_VALUE_SET)
    public void handleDataValueSet(MotechEvent event) {
        dhisService.sendDataValueSet(event.getParameters());
    }
}
