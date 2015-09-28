package org.motechproject.messagecampaign.it.dao;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.commons.date.util.JodaFormatter;
import org.motechproject.messagecampaign.dao.CampaignMessageRecordService;
import org.motechproject.messagecampaign.dao.CampaignRecordService;
import org.motechproject.messagecampaign.domain.campaign.AbsoluteCampaign;
import org.motechproject.messagecampaign.domain.campaign.CampaignType;
import org.motechproject.messagecampaign.domain.campaign.CronBasedCampaign;
import org.motechproject.messagecampaign.domain.campaign.OffsetCampaign;
import org.motechproject.messagecampaign.domain.campaign.AbsoluteCampaignMessage;
import org.motechproject.messagecampaign.domain.campaign.CampaignMessage;
import org.motechproject.messagecampaign.domain.campaign.CronBasedCampaignMessage;
import org.motechproject.messagecampaign.domain.campaign.OffsetCampaignMessage;
import org.motechproject.messagecampaign.domain.campaign.CampaignMessageRecord;
import org.motechproject.messagecampaign.domain.campaign.CampaignRecord;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class CampaignRecordServiceBundleIT extends BasePaxIT {

    @Inject
    private CampaignRecordService campaignRecordService;

    @Inject
    private CampaignMessageRecordService campaignMessageRecordService;

    @Test
    public void getAbsoluteDatesMessageProgramTest() {
        String campaignName = "Absolute Dates Message Program";

        AbsoluteCampaign campaign = (AbsoluteCampaign) campaignRecordService.findByName(campaignName).toCampaign();
        assertNotNull(campaign);
        assertEquals(campaignName, campaign.getName());
        List<AbsoluteCampaignMessage> messages = campaign.getMessages();
        assertEquals(2, messages.size());
        DateTime firstDate = new DateTime(2013, 6, 15, 0, 0, 0, 0);
        DateTime secondDate = new DateTime(2013, 6, 22, 0, 0, 0, 0);
        assertMessageWithAbsoluteSchedule(messages.get(0), "First", new String[]{"IVR", "SMS"}, "random-1", firstDate.toLocalDate());
        assertMessageWithAbsoluteSchedule(messages.get(1), "Second", new String[]{"IVR"}, "random-2", secondDate.toLocalDate());
    }

    @Test
    public void getRelativeDatesMessageProgramTest() {
        String campaignName = "Relative Dates Message Program";

        OffsetCampaign campaign = (OffsetCampaign) campaignRecordService.findByName(campaignName).toCampaign();
        assertNotNull(campaign);
        assertEquals(campaignName, campaign.getName());
        List<OffsetCampaignMessage> messages = campaign.getMessages();
        assertEquals(3, messages.size());
        assertMessageWithRelativeSchedule(messages.get(0), "Week 1", new String[]{"IVR"}, "child-info-week-1", "1 Week");
        assertMessageWithRelativeSchedule(messages.get(1), "Week 1A", new String[]{"SMS"}, "child-info-week-1a", "1 Week");
        assertMessageWithRelativeSchedule(messages.get(2), "Week 1B", new String[]{"SMS"}, "child-info-week-1b", "9 Days");
    }

    @Test
    public void getCronBasedMessageProgramTest() {
        String campaignName = "Cron based Message Program";

        CronBasedCampaign campaign = (CronBasedCampaign) campaignRecordService.findByName(campaignName).toCampaign();
        assertNotNull(campaign);
        assertEquals(campaignName, campaign.getName());
        assertEquals(new JodaFormatter().parsePeriod("5 years"), campaign.getMaxDuration());
        List<CronBasedCampaignMessage> messages = campaign.getMessages();
        assertEquals(1, messages.size());
        assertMessageWithCronSchedule(messages.get(0), "First", new String[]{"IVR", "SMS"}, "cron-message", "0 11 11 11 11 ?");
    }

    @Test
    public void shouldUpdateRecord() {

        final CampaignRecord campaign = createCampaignRecord();
        final CampaignRecord campaign2 = createCampaignRecord();

        // add
        campaignRecordService.create(campaign);

        assertEquals(campaign, campaignRecordService.findByName(campaign.getName()));

        campaignRecordService.doInTransaction(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                // update
                CampaignRecord campaignToUpdate = campaignRecordService.findByName(campaign.getName());
                campaign2.setMaxDuration("20 week");
                campaignToUpdate.updateFrom(campaign2);

                campaignRecordService.update(campaignToUpdate);
            }
        });

        assertEquals("20 week", campaignRecordService.findByName(campaign.getName()).getMaxDuration());

        campaignRecordService.delete(campaign);
    }

    @Test
    public void shouldDeleteCampaignRecords() {

        CampaignRecord campaign = createCampaignRecord();

        campaignRecordService.create(campaign);

        assertEquals(campaign, campaignRecordService.findByName(campaign.getName()));

        campaignRecordService.delete(campaign);

        assertNull(campaignRecordService.findByName(campaign.getName()));
    }

    @Test
    public void shouldFindCampaignsByName() {

        CampaignRecord campaign = createCampaignRecord();
        CampaignRecord campaign2 = createCampaignRecord();
        campaign2.setName("Different Name");

        campaignRecordService.create(campaign);
        campaignRecordService.create(campaign2);

        assertEquals(campaign, campaignRecordService.findByName("CampaignName"));
        assertEquals(campaign2, campaignRecordService.findByName("Different Name"));

        campaignRecordService.delete(campaign);
        campaignRecordService.delete(campaign2);
    }

    private void assertMessageWithAbsoluteSchedule(AbsoluteCampaignMessage message, String name, String[] formats, Object messageKey, LocalDate date) {
        assertMessage(message, name, formats, messageKey);
        assertEquals(date, message.getDate());
    }

    private void assertMessageWithRelativeSchedule(OffsetCampaignMessage message, String name, String[] formats, Object messageKey, String timeOffset) {
        assertMessage(message, name, formats, messageKey);
        assertEquals(new JodaFormatter().parsePeriod(timeOffset), message.getTimeOffset());
    }

    private void assertMessageWithCronSchedule(CronBasedCampaignMessage message, String name, String[] formats, Object messageKey, String cron) {
        assertMessage(message, name, formats, messageKey);
        assertEquals(cron, message.getCron());
    }

    private void assertMessage(CampaignMessage message, String name, String[] formats, Object messageKey) {
        assertEquals(name, message.getName());
        assertCollection(formats, message.getFormats());
        assertCollection(new String[]{"en"}, message.getLanguages());
        assertEquals(messageKey, message.getMessageKey());
    }

    private void assertCollection(String[] expectedFormats, List<String> actualFormats) {
        assertEquals(new HashSet<>(asList(expectedFormats)), new HashSet<>(actualFormats));
    }

    private CampaignRecord createCampaignRecord() {
        CampaignRecord campaign = new CampaignRecord();
        campaign.setCampaignType(CampaignType.ABSOLUTE);
        campaign.setMaxDuration("10 week");
        campaign.setName("CampaignName");

        CampaignMessageRecord message = new CampaignMessageRecord();
        message.setDate(LocalDate.now());
        message.setMessageType(CampaignType.ABSOLUTE);
        message.setStartTime("20:44");
        message.setMessageKey("key");
        message.setLanguages(new ArrayList<>(asList("lang1", "lang2", "lang3")));

        campaign.setMessages(new ArrayList<>(asList(message)));

        return campaign;
    }
}
