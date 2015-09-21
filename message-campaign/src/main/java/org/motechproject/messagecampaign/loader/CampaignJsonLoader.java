package org.motechproject.messagecampaign.loader;

import com.google.gson.reflect.TypeToken;
import org.motechproject.commons.api.MotechException;
import org.motechproject.commons.api.json.MotechJsonReader;
import org.motechproject.messagecampaign.dao.CampaignRecordService;
import org.motechproject.messagecampaign.domain.campaign.CampaignRecord;
import org.motechproject.messagecampaign.web.model.CampaignDto;
import org.motechproject.server.config.SettingsFacade;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for loading the message campaigns from resource files.
 */
public class CampaignJsonLoader {

    private String messageCampaignsJsonFile = "message-campaigns.json";

    private SettingsFacade settings;

    private CampaignRecordService campaignRecordService;

    private MotechJsonReader motechJsonReader;

    public CampaignJsonLoader() {
        this(new MotechJsonReader());
    }

    public CampaignJsonLoader(MotechJsonReader motechJsonReader) {
        this.motechJsonReader = motechJsonReader;
    }

    /**
     * Loads message campaigns from the file of the given name.
     *
     * @param filename the name of the file to load the campaigns from
     * @return a list of loaded campaigns
     */
    public List<CampaignRecord> loadCampaigns(String filename) {
        try (InputStream in = this.getClass().getClassLoader().getResourceAsStream(filename)) {
            return loadCampaigns(in);
        } catch (IOException e) {
            throw new MotechException("Error while loading json file", e);
        }
    }

    /**
     * Loads message campaigns from the given input stream.
     *
     * @param in the input stream to load the campaigns from
     * @return a list of loaded campaigns
     */
    public List<CampaignRecord> loadCampaigns(InputStream in) {
        List<CampaignDto> dtoList = (List<CampaignDto>) motechJsonReader.readFromStream(
                in, new TypeToken<List<CampaignDto>>() {
        }.getType()
        );

        List<CampaignRecord> records = new ArrayList<>(dtoList.size());
        for (CampaignDto dto : dtoList) {
            records.add(dto.toCampaignRecord());
        }

        return records;
    }

    /**
     * Loads single message campaign definition from the given input stream.
     *
     * @param in the input stream to load the campaign from
     * @return a loaded campaign
     */
    public CampaignRecord loadSingleCampaign(InputStream in) {
        CampaignDto campaignDto = (CampaignDto) motechJsonReader.readFromStream(
                in, new TypeToken<CampaignDto>() {
        }.getType()
        );
        return campaignDto.toCampaignRecord();
    }

    /**
     * Loads single message campaign definition from the file of the given name.
     *
     * @param filename the name of the file to load the campaigns from
     * @return a loaded campaign
     */
    public CampaignRecord loadSingleCampaign(String filename) {
        try (InputStream in = this.getClass().getClassLoader().getResourceAsStream(filename)) {
            return loadSingleCampaign(in);
        } catch (IOException e) {
            throw new MotechException("Error while loading json file", e);
        }
    }

    /**
     * Loads and saves the message campaigns from the default location, provided the campaign
     * if the given name does not exist yet.
     */
    public void loadAfterInit() {
        List<CampaignRecord> records = loadCampaigns(settings.getRawConfig(messageCampaignsJsonFile));
        for (CampaignRecord record : records) {
            if (campaignRecordService.findByName(record.getName()) == null) {
                campaignRecordService.create(record);
            }
        }
    }

    public String getMessageCampaignsJsonFile() {
        return messageCampaignsJsonFile;
    }

    public void setMessageCampaignsJsonFile(String messageCampaignsJsonFile) {
        this.messageCampaignsJsonFile = messageCampaignsJsonFile;
    }

    public void setSettings(@Qualifier("messageCampaignSettings") SettingsFacade settings) {
        this.settings = settings;
    }

    public void setAllMessageCampaigns(CampaignRecordService campaignRecordService) {
        this.campaignRecordService = campaignRecordService;
    }
}
