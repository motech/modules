package org.motechproject.scheduletracking.service.impl;

import org.joda.time.LocalDate;
import org.motechproject.commons.date.model.Time;
import org.motechproject.scheduletracking.domain.Enrollment;
import org.motechproject.scheduletracking.domain.EnrollmentStatus;
import org.motechproject.scheduletracking.domain.Schedule;
import org.motechproject.scheduletracking.domain.ScheduleFactory;
import org.motechproject.scheduletracking.domain.exception.InvalidEnrollmentException;
import org.motechproject.scheduletracking.domain.exception.ScheduleTrackingException;
import org.motechproject.scheduletracking.domain.json.ScheduleRecord;
import org.motechproject.scheduletracking.repository.TrackedSchedulesJsonReader;
import org.motechproject.scheduletracking.repository.TrackedSchedulesJsonReaderImpl;
import org.motechproject.scheduletracking.repository.dataservices.EnrollmentDataService;
import org.motechproject.scheduletracking.repository.dataservices.ScheduleDataService;
import org.motechproject.scheduletracking.service.EnrollmentRecord;
import org.motechproject.scheduletracking.service.EnrollmentRequest;
import org.motechproject.scheduletracking.service.EnrollmentService;
import org.motechproject.scheduletracking.service.EnrollmentsQuery;
import org.motechproject.scheduletracking.service.EnrollmentUpdater;
import org.motechproject.scheduletracking.service.MilestoneAlerts;
import org.motechproject.scheduletracking.service.ScheduleTrackingService;
import org.motechproject.scheduletracking.service.contract.UpdateCriteria;
import org.motechproject.scheduletracking.service.contract.UpdateCriterion;
import org.motechproject.server.config.SettingsFacade;
import org.motechproject.server.config.domain.MotechSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.text.MessageFormat.format;
import static org.motechproject.commons.date.util.DateUtil.newDateTime;

/**
 * Implementation of {@link org.motechproject.scheduletracking.service.ScheduleTrackingService}.
 */
public class ScheduleTrackingServiceImpl implements ScheduleTrackingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleTrackingServiceImpl.class);
    private EnrollmentDataService enrollmentDataService;
    private ScheduleDataService scheduleDataService;
    private EnrollmentService enrollmentService;
    private EnrollmentsQueryService enrollmentsQueryService;
    private EnrollmentRecordMapper enrollmentRecordMapper;
    private TrackedSchedulesJsonReader schedulesJsonReader;
    private SettingsFacade scheduleTrackingSettings;

    @Autowired
    public ScheduleTrackingServiceImpl(EnrollmentsQueryService enrollmentsQueryService,
                                       ScheduleDataService scheduleDataService, EnrollmentService enrollmentService,
                                       EnrollmentRecordMapper enrollmentRecordMapper, EnrollmentDataService enrollmentDataService,
                                       @Qualifier("scheduleTrackingSettings") SettingsFacade scheduleTrackingSettings) {
        this.enrollmentDataService = enrollmentDataService;
        this.scheduleDataService = scheduleDataService;
        this.enrollmentService = enrollmentService;
        this.enrollmentsQueryService = enrollmentsQueryService;
        this.enrollmentRecordMapper = enrollmentRecordMapper;
        this.scheduleTrackingSettings = scheduleTrackingSettings;
        this.schedulesJsonReader = new TrackedSchedulesJsonReaderImpl();
    }

    @Override
    @Transactional
    public EnrollmentRecord getEnrollment(String externalId, String scheduleName) {
        LOGGER.info("Fetching Active Enrollment based on externalId {} and {}", externalId, scheduleName);
        Enrollment activeEnrollment = enrollmentDataService.findByExternalIdScheduleNameAndStatus(externalId, scheduleName,
                EnrollmentStatus.ACTIVE);
        return enrollmentRecordMapper.map(activeEnrollment);
    }

    @Override
    @Transactional
    public void updateEnrollment(String externalId, String scheduleName, UpdateCriteria updateCriteria) {
        Enrollment enrollment = enrollmentDataService.findByExternalIdScheduleNameAndStatus(externalId, scheduleName,
                EnrollmentStatus.ACTIVE);
        if (enrollment == null) {
            throw new InvalidEnrollmentException(
                    format("Cannot find an active enrollment with " +
                            "External ID: {0} & Schedule name: {1}", externalId, scheduleName));
        } else {
            Map<UpdateCriterion, Object> criteria = updateCriteria.getAll();
            for (Map.Entry<UpdateCriterion, Object> entry : criteria.entrySet()) {
                EnrollmentUpdater.get(entry.getKey()).update(enrollment, entry.getValue());
            }
            LOGGER.info("Updating Enrollment {}.", enrollment.getId());
            enrollmentDataService.update(enrollment);
        }
    }

    @Override
    @Transactional
    public List<EnrollmentRecord> search(EnrollmentsQuery query) {
        List<EnrollmentRecord> enrollmentRecords = new ArrayList<EnrollmentRecord>();
        for (Enrollment enrollment : enrollmentsQueryService.search(query)) {
            enrollmentRecords.add(enrollmentRecordMapper.map(enrollment));
        }
        return enrollmentRecords;
    }

    @Override
    @Transactional
    public List<EnrollmentRecord> searchWithWindowDates(EnrollmentsQuery query) {
        List<EnrollmentRecord> enrollmentRecords = new ArrayList<EnrollmentRecord>();
        for (Enrollment enrollment : enrollmentsQueryService.search(query)) {
            enrollmentRecords.add(enrollmentRecordMapper.mapWithDates(enrollment));
        }
        return enrollmentRecords;
    }

    @Override
    @Transactional
    public MilestoneAlerts getAlertTimings(EnrollmentRequest enrollmentRequest) {
        Schedule schedule = scheduleDataService.findByName(enrollmentRequest.getScheduleName());
        if (schedule == null) {
            throw new ScheduleTrackingException("No schedule with name: %s", enrollmentRequest.getScheduleName());
        }

        String startingMilestoneName;
        if (enrollmentRequest.isStartingMilestoneSpecified()) {
            startingMilestoneName = enrollmentRequest.getStartingMilestoneName();
        } else {
            startingMilestoneName = schedule.getFirstMilestone().getName();
        }

        return enrollmentService.getAlertTimings(enrollmentRequest.getExternalId(), enrollmentRequest.getScheduleName(),
                startingMilestoneName, enrollmentRequest.getReferenceDateTime(), enrollmentRequest.getEnrollmentDateTime(),
                enrollmentRequest.getPreferredAlertTime());
    }

    @Override
    @Transactional
    public void add(String scheduleJson) {
        LOGGER.info("Creating a schedule record from schedule json {}", scheduleJson);
        ScheduleRecord scheduleRecord = schedulesJsonReader.getSchedule(scheduleJson);
        Schedule schedule = new ScheduleFactory().build(scheduleRecord, getLanguage());

        Schedule existing = scheduleDataService.findByName(schedule.getName());

        if (existing == null) {
            LOGGER.info("Saving a schedule record with schedule name {}.", schedule.getName());
            scheduleDataService.create(schedule);
        } else {
            LOGGER.info("Updating a schedule record with schedule name {}.", existing.getName());
            existing.merge(schedule);
            scheduleDataService.update(existing);
        }
    }

    @Override
    @Transactional
    public void remove(String scheduleName) {
        LOGGER.info("Deleting a schedule record with schedule name {}.", scheduleName);
        scheduleDataService.delete(scheduleDataService.findByName(scheduleName));
    }

    @Override
    @Transactional
    public Long enroll(EnrollmentRequest enrollmentRequest) {
        Schedule schedule = scheduleDataService.findByName(enrollmentRequest.getScheduleName());
        if (schedule == null) {
            throw new ScheduleTrackingException("No schedule with name: %s", enrollmentRequest.getScheduleName());
        }

        String startingMilestoneName;
        if (enrollmentRequest.isStartingMilestoneSpecified()) {
            startingMilestoneName = enrollmentRequest.getStartingMilestoneName();
        } else {
            startingMilestoneName = schedule.getFirstMilestone().getName();
        }
        LOGGER.info("Enrolling a enrollment record with externalID {} and starting milestone {}."
                , enrollmentRequest.getExternalId(), enrollmentRequest.getStartingMilestoneName());
        return enrollmentService.enroll(enrollmentRequest.getExternalId(), enrollmentRequest.getScheduleName(), startingMilestoneName,
                enrollmentRequest.getReferenceDateTime(), enrollmentRequest.getEnrollmentDateTime(), enrollmentRequest.getPreferredAlertTime(),
                enrollmentRequest.getMetadata());
    }

    @Override
    @Transactional
    public void fulfillCurrentMilestone(String externalId, String scheduleName, LocalDate fulfillmentDate, Time fulfillmentTime) {
        Enrollment activeEnrollment = enrollmentDataService.findByExternalIdScheduleNameAndStatus(externalId, scheduleName,
                EnrollmentStatus.ACTIVE);
        if (activeEnrollment == null) {
            throw new InvalidEnrollmentException(format("Can fulfill only active enrollments. This enrollment has: External ID: {0}, Schedule name: {1}",
                    externalId, scheduleName));
        }
        if (isDuplicateFulfillment(activeEnrollment, fulfillmentDate, fulfillmentTime)) {
            LOGGER.info("Returning as milestone is a duplicate milestone.");
            return;
        }
        LOGGER.info("Fulfilling a milestone with enrollment id {}.", activeEnrollment.getId());
        enrollmentService.fulfillCurrentMilestone(activeEnrollment, newDateTime(fulfillmentDate, fulfillmentTime));
    }

    private boolean isDuplicateFulfillment(Enrollment activeEnrollment, LocalDate fulfillmentDate, Time fulfillmentTime) {
        return activeEnrollment.getFulfillments().size() > 0 && activeEnrollment.getLastFulfilledDate().equals(newDateTime(fulfillmentDate, fulfillmentTime));
    }

    @Override
    public void fulfillCurrentMilestone(String externalId, String scheduleName, LocalDate fulfillmentDate) {
        fulfillCurrentMilestone(externalId, scheduleName, fulfillmentDate, new Time(0, 0));
    }

    @Override
    @Transactional
    public void unenroll(String externalId, List<String> scheduleNames) {
        LOGGER.info("Un-enrolling a enrollment with external Id {} and schedule names {} .", externalId, scheduleNames);
        for (String scheduleName : scheduleNames) {
            Enrollment activeEnrollment = enrollmentDataService.findByExternalIdScheduleNameAndStatus(externalId, scheduleName,
                    EnrollmentStatus.ACTIVE);
            if (activeEnrollment != null) {
                enrollmentService.unenroll(activeEnrollment);
            }
        }
    }

    @Override
    @Transactional
    public Schedule getScheduleByName(String scheduleName) {
        return scheduleDataService.findByName(scheduleName);
    }

    @Override
    @Transactional
    public List<Schedule> getAllSchedules() {
        return scheduleDataService.retrieveAll();
    }

    private Locale getLanguage() {
        MotechSettings settings = scheduleTrackingSettings.getPlatformSettings();
        return (settings == null) ? Locale.ENGLISH : new Locale(settings.getLanguage());
    }
}
