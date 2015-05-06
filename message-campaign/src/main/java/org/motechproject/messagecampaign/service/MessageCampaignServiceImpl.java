package org.motechproject.messagecampaign.service;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.motechproject.config.core.constants.ConfigurationConstants;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.messagecampaign.EventKeys;
import org.motechproject.messagecampaign.contract.CampaignRequest;
import org.motechproject.messagecampaign.dao.CampaignEnrollmentDataService;
import org.motechproject.messagecampaign.dao.CampaignMessageRecordService;
import org.motechproject.messagecampaign.dao.CampaignRecordService;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollmentStatus;
import org.motechproject.messagecampaign.domain.campaign.CampaignMessageRecord;
import org.motechproject.messagecampaign.exception.CampaignNotFoundException;
import org.motechproject.messagecampaign.domain.campaign.Campaign;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollment;
import org.motechproject.messagecampaign.domain.campaign.CampaignMessage;
import org.motechproject.messagecampaign.loader.CampaignJsonLoader;
import org.motechproject.messagecampaign.scheduler.CampaignSchedulerFactory;
import org.motechproject.messagecampaign.scheduler.CampaignSchedulerService;
import org.motechproject.messagecampaign.domain.campaign.CampaignRecord;
import org.motechproject.messagecampaign.exception.EnrollmentNotFoundException;
import org.motechproject.scheduler.contract.JobId;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.motechproject.server.config.SettingsFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link MessageCampaignService}
 */
@Service("messageCampaignService")
public class MessageCampaignServiceImpl implements MessageCampaignService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageCampaignServiceImpl.class);

    private EnrollmentService enrollmentService;
    private CampaignEnrollmentRecordMapper campaignEnrollmentRecordMapper;
    private CampaignEnrollmentDataService campaignEnrollmentDataService;
    private CampaignSchedulerFactory campaignSchedulerFactory;
    private CampaignRecordService campaignRecordService;
    private CampaignMessageRecordService campaignMessageRecordService;
    private EventRelay relay;
    private MotechSchedulerService schedulerService;

    @Autowired
    @Qualifier("messageCampaignSettings")
    private SettingsFacade settingsFacade;

    @Autowired
    private CommonsMultipartResolver commonsMultipartResolver;

    @Autowired
    public MessageCampaignServiceImpl(EnrollmentService enrollmentService, CampaignEnrollmentDataService campaignEnrollmentDataService, CampaignEnrollmentRecordMapper campaignEnrollmentRecordMapper,
                                      CampaignSchedulerFactory campaignSchedulerFactory, CampaignRecordService campaignRecordService, CampaignMessageRecordService campaignMessageRecordService,
                                      EventRelay relay, MotechSchedulerService schedulerService) {
        this.enrollmentService = enrollmentService;
        this.campaignEnrollmentDataService = campaignEnrollmentDataService;
        this.campaignEnrollmentRecordMapper = campaignEnrollmentRecordMapper;
        this.campaignEnrollmentDataService = campaignEnrollmentDataService;
        this.campaignSchedulerFactory = campaignSchedulerFactory;
        this.campaignRecordService = campaignRecordService;
        this.campaignMessageRecordService = campaignMessageRecordService;
        this.relay = relay;
        this.schedulerService = schedulerService;
    }

    @Override
    public void enroll(CampaignRequest request) {
        CampaignEnrollment enrollment = new CampaignEnrollment(request.externalId(), request.campaignName());
        enrollment.setReferenceDate(request.referenceDate());
        enrollment.setDeliverTime(request.deliverTime());

        CampaignSchedulerService campaignScheduler = campaignSchedulerFactory.getCampaignScheduler(request.campaignName());
        campaignScheduler.start(enrollment);

        enrollmentService.register(enrollment);

        Map<String, Object> param = new HashMap<>();
        param.put(EventKeys.EXTERNAL_ID_KEY, enrollment.getExternalId());
        param.put(EventKeys.CAMPAIGN_NAME_KEY, enrollment.getCampaignName());
        MotechEvent event = new MotechEvent(EventKeys.ENROLLED_USER_SUBJECT, param);

        relay.sendEventMessage(event);
    }

    @Override
    public void unenroll(String externalId, String campaignName) {
        CampaignEnrollment enrollment = campaignEnrollmentDataService.findByExternalIdAndCampaignName(externalId, campaignName);

        if (enrollment != null) {
            campaignSchedulerFactory.getCampaignScheduler(campaignName).stop(enrollment);
            enrollmentService.unregister(externalId, campaignName);

            Map<String, Object> param = new HashMap<>();
            param.put(EventKeys.EXTERNAL_ID_KEY, externalId);
            param.put(EventKeys.CAMPAIGN_NAME_KEY, campaignName);
            MotechEvent event = new MotechEvent(EventKeys.UNENROLLED_USER_SUBJECT, param);

            relay.sendEventMessage(event);
        } else {
            LOGGER.warn("No enrollment with ExternalID {} registered in campaign {}", externalId, campaignName);
        }
    }

    @Override
    public List<CampaignEnrollmentRecord> search(CampaignEnrollmentsQuery query) {
        List<CampaignEnrollmentRecord> campaignEnrollmentRecords = new ArrayList<>();
        for (CampaignEnrollment campaignEnrollment : enrollmentService.search(query)) {
            campaignEnrollmentRecords.add(campaignEnrollmentRecordMapper.map(campaignEnrollment));
        }
        return campaignEnrollmentRecords;
    }

    @Override
    public Map<String, List<DateTime>> getCampaignTimings(String externalId, String campaignName, DateTime startDate, DateTime endDate) {
        CampaignEnrollment enrollment = campaignEnrollmentDataService.findByExternalIdAndCampaignName(externalId, campaignName);
        if (!enrollment.isActive()) {
            return new HashMap<>();
        }
        return campaignSchedulerFactory.getCampaignScheduler(campaignName).getCampaignTimings(startDate, endDate, enrollment);
    }

    @Override
    public void updateEnrollment(CampaignRequest enrollRequest, Long enrollmentId) {
        CampaignEnrollment existingEnrollment = campaignEnrollmentDataService.findById(enrollmentId);

        if (existingEnrollment == null) {
            throw new EnrollmentNotFoundException("Enrollment with id " + enrollmentId + " not found");
        } else {
            CampaignEnrollment campaignEnrollment = campaignEnrollmentDataService.findByExternalIdAndCampaignName(
                    enrollRequest.externalId(), enrollRequest.campaignName());
            if (campaignEnrollment != null && !existingEnrollment.getExternalId().equals(campaignEnrollment.getExternalId())) {
                throw new IllegalArgumentException(String.format("%s is already enrolled in %s campaign",
                        enrollRequest.externalId(), enrollRequest.campaignName()));
            }
        }

        campaignSchedulerFactory.getCampaignScheduler(existingEnrollment.getCampaignName()).stop(existingEnrollment);

        existingEnrollment.setExternalId(enrollRequest.externalId());
        existingEnrollment.setDeliverTime(enrollRequest.deliverTime());
        existingEnrollment.setReferenceDate(enrollRequest.referenceDate());
        campaignEnrollmentDataService.update(existingEnrollment);

        campaignSchedulerFactory.getCampaignScheduler(existingEnrollment.getCampaignName()).start(existingEnrollment);
    }

    @Override
    public void stopAll(CampaignEnrollmentsQuery query) {
        stopAll(query, false);
    }

    @Override
    public void saveCampaign(CampaignRecord campaign) {
        CampaignRecord record = campaignRecordService.findByName(campaign.getName());
        if (record == null) {
            campaignRecordService.create(campaign);
        }
    }

    @Override
    public void deleteCampaign(String campaignName) {
        CampaignRecord campaignRecord = campaignRecordService.findByName(campaignName);

        if (campaignRecord == null) {
            throw new CampaignNotFoundException("Campaign not found: " + campaignName);
        } else {
            CampaignEnrollmentsQuery enrollmentsQuery = new CampaignEnrollmentsQuery().withCampaignName(campaignName);
            stopAll(enrollmentsQuery, true);

            campaignRecordService.delete(campaignRecord);
        }
    }

    @Override
    public String getLatestCampaignMessage(String campaignName, String externalId) {
        CampaignEnrollment enrollment = campaignEnrollmentDataService.findByExternalIdAndCampaignName(externalId, campaignName);
        Campaign campaign = campaignRecordService.findByName(enrollment.getCampaignName()).toCampaign();
        DateTime latestDate = null;
        CampaignMessage latestMessage = null;

        for (Object message : campaign.getMessages()) {
            CampaignMessage campaignMessage = (CampaignMessage) message;
            CampaignSchedulerService campaignSchedulerService = campaignSchedulerFactory.getCampaignScheduler(enrollment.getCampaignName());
            JobId jobId = campaignSchedulerService.getJobId(campaignMessage.getMessageKey(),
                    enrollment.getExternalId(), enrollment.getCampaignName());
            DateTime date = schedulerService.getPreviousFireDate(jobId);

            if (date == null || date.isAfterNow()) {
                continue;
            }

            if (latestMessage == null) {
                latestMessage = campaignMessage;
                latestDate = date;
            } else if (latestDate.isBefore(date)) {
                latestDate = date;
                latestMessage = campaignMessage;
            }
        }
        return (latestMessage == null) ? null : latestMessage.getMessageKey();
    }

    @Override
    public String getNextCampaignMessage(String campaignName, String externalId) {
        CampaignEnrollment enrollment = campaignEnrollmentDataService.findByExternalIdAndCampaignName(externalId, campaignName);
        Campaign campaign = campaignRecordService.findByName(enrollment.getCampaignName()).toCampaign();
        DateTime nextDate = null;
        CampaignMessage nextMessage = null;

        for (Object message : campaign.getMessages()) {
            CampaignMessage campaignMessage = (CampaignMessage) message;
            CampaignSchedulerService campaignSchedulerService = campaignSchedulerFactory.getCampaignScheduler(enrollment.getCampaignName());
            JobId jobId = campaignSchedulerService.getJobId(campaignMessage.getMessageKey(),
                    enrollment.getExternalId(), enrollment.getCampaignName());
            DateTime date = schedulerService.getNextFireDate(jobId);

            if (date == null || date.isBeforeNow()) {
                continue;
            }

            if (nextMessage == null) {
                nextMessage = campaignMessage;
                nextDate = date;
            } else if (nextDate.isAfter(date)) {
                nextDate = date;
                nextMessage = campaignMessage;
            }
        }
        return (nextMessage == null) ? null : nextMessage.getMessageKey();
    }

    @Override
    public CampaignRecord getCampaignRecord(String campaignName) {
        return campaignRecordService.findByName(campaignName);
    }

    @Override
    public List<CampaignRecord> getAllCampaignRecords() {
        return campaignRecordService.retrieveAll();
    }

    @Override
    public void campaignCompleted(String externalId, String campaignName) {
        unenroll(externalId, campaignName);
    }

    @Override
    public void loadCampaigns() throws IOException {
        try (InputStream inputStream = settingsFacade.getRawConfig(MESSAGE_CAMPAIGNS_JSON_FILENAME)) {
            List<CampaignRecord> records = new CampaignJsonLoader().loadCampaigns(inputStream);
            for (CampaignRecord campaign : records) {
                campaign.toCampaign().validate();
                CampaignRecord record = campaignRecordService.findByName(campaign.getName());
                if (record == null) {
                    campaignRecordService.create(campaign);
                } else {
                    record.setCampaignType(campaign.getCampaignType());
                    record.setMaxDuration(campaign.getMaxDuration());
                    record.setMessages(campaign.getMessages());
                    campaignRecordService.update(record);
                    List<CampaignEnrollment> enrollments = campaignEnrollmentDataService.findByCampaignName(record.getName());
                    for (CampaignEnrollment enrollment : enrollments) {
                        CampaignRequest request = new CampaignRequest();
                        request.setCampaignName(record.getName());
                        request.setExternalId(enrollment.getExternalId());
                        request.setStartTime(enrollment.getDeliverTime());
                        request.setReferenceDate(enrollment.getReferenceDate());
                        updateEnrollment(request, enrollment.getId());
                    }
                }
            }
        }
    }

    @Override
    public void updateEnrollments(Long campaignId) {
        CampaignRecord campaign = campaignRecordService.findById(campaignId);
        if (campaign == null) {
            throw new IllegalArgumentException("Couldn't find CampaignRecord with id: " + campaignId);
        }

        CampaignEnrollmentsQuery query = new CampaignEnrollmentsQuery()
                .withCampaignName(campaign.getName())
                .havingState(CampaignEnrollmentStatus.ACTIVE);

        List<CampaignEnrollment> campaignEnrollments = enrollmentService.search(query);

        for (CampaignEnrollment campaignEnrollment : campaignEnrollments) {
            campaignSchedulerFactory.getCampaignScheduler(campaignEnrollment.getCampaignName()).stop(campaignEnrollment);

            campaignSchedulerFactory.getCampaignScheduler(campaignEnrollment.getCampaignName()).start(campaignEnrollment);
        }
    }

    @Override
    public void unscheduleMessageJob(CampaignMessageRecord campaignMessageRecord) {
        if (campaignMessageRecord.getCampaign() != null) {
            Campaign campaign = campaignMessageRecord.getCampaign().toCampaign();

            CampaignEnrollmentsQuery query = new CampaignEnrollmentsQuery()
                    .withCampaignName(campaign.getName())
                    .havingState(CampaignEnrollmentStatus.ACTIVE);
            List<CampaignEnrollment> campaignEnrollments = enrollmentService.search(query);

            CampaignMessage campaignMessage = campaign.getCampaignMessage(campaignMessageRecord);

            for (CampaignEnrollment campaignEnrollment : campaignEnrollments) {
                campaignSchedulerFactory.getCampaignScheduler(campaignEnrollment.getCampaignName()).unscheduleMessageJob(campaignEnrollment, campaignMessage);
            }
        }
    }

    @Override
    public void rescheduleMessageJob(Long campaignMessageRecordId) {
        CampaignMessageRecord campaignMessageRecord = campaignMessageRecordService.findById(campaignMessageRecordId);
        if (campaignMessageRecord == null) {
            throw new IllegalArgumentException("Couldn't find CampaignMessageRecord with id: " + campaignMessageRecordId);
        }

        CampaignRecord campaignRecord = campaignMessageRecord.getCampaign();
        if (campaignRecord != null) {
            CampaignEnrollmentsQuery query = new CampaignEnrollmentsQuery()
                    .withCampaignName(campaignRecord.getName())
                    .havingState(CampaignEnrollmentStatus.ACTIVE);
            List<CampaignEnrollment> campaignEnrollments = enrollmentService.search(query);

            Campaign campaign = campaignRecord.toCampaign();
            CampaignMessage campaignMessage = campaign.getCampaignMessage(campaignMessageRecord);

            for (CampaignEnrollment campaignEnrollment : campaignEnrollments) {
                campaignSchedulerFactory.getCampaignScheduler(campaignRecord.getName()).rescheduleMessageJob(campaignEnrollment, campaign, campaignMessage);
            }
        }
    }

    @PostConstruct
    public void loadCampaignsJson() {
        try (InputStream inputStream = settingsFacade.getRawConfig(MESSAGE_CAMPAIGNS_JSON_FILENAME)) {
            List<CampaignRecord> records = new CampaignJsonLoader().loadCampaigns(inputStream);
            for (CampaignRecord record : records) {
                record.toCampaign().validate();
                if(campaignRecordService.findByName(record.getName()) == null) {
                    campaignRecordService.create(record);
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error while reading Message Campaign JSON file", e);
        }
    }

    @MotechListener(subjects = ConfigurationConstants.FILE_CHANGED_EVENT_SUBJECT)
    public void changeMaxUploadSize(MotechEvent event) {
        String uploadSize = settingsFacade.getPlatformSettings().getUploadSize();

        if (StringUtils.isNotBlank(uploadSize)) {
            commonsMultipartResolver.setMaxUploadSize(Long.valueOf(uploadSize));
        }
    }

    @Override
    public void stopAll(CampaignEnrollmentsQuery query, boolean deleteEnrollments) {
        List<CampaignEnrollment> enrollments = enrollmentService.search(query);
        for (CampaignEnrollment enrollment : enrollments) {
            campaignSchedulerFactory.getCampaignScheduler(enrollment.getCampaignName()).stop(enrollment);

            if (deleteEnrollments) {
                enrollmentService.delete(enrollment);
            } else {
                enrollmentService.unregister(enrollment.getExternalId(), enrollment.getCampaignName());
            }
        }
    }
}
