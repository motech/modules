package org.motechproject.ipf.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import org.motechproject.ipf.domain.IPFRecipient;
import org.motechproject.ipf.service.IPFRecipientsService;
import org.motechproject.ipf.util.Constants;
import org.motechproject.server.config.SettingsFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("ipfRecipientsService")
public class IPFRecipientsServiceImpl implements IPFRecipientsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IPFRecipientsServiceImpl.class);

    @Autowired
    @Qualifier("ipfSettings")
    private SettingsFacade settingsFacade;

    private Map<String, IPFRecipient> recipients;

    @PostConstruct
    public void init() {
        recipients = new HashMap<>();
        List<IPFRecipient> recipientsList;
        try (InputStream is = settingsFacade.getRawConfig(Constants.IPF_RECIPIENTS_FILE)) {
            String jsonText = IOUtils.toString(is);
            LOGGER.debug("Loading {}", Constants.IPF_RECIPIENTS_FILE);
            Gson gson = new Gson();
            recipientsList = gson.fromJson(jsonText, new TypeToken<List<IPFRecipient>>() { } .getType());
        } catch (Exception e) {
            String message = String.format("There seems to be a problem with the json text in %s: %s", Constants.IPF_RECIPIENTS_FILE,
                    e.getMessage());
            LOGGER.debug(message);
            throw new JsonIOException(message, e);
        }

        for (IPFRecipient recipient : recipientsList) {
            recipients.put(recipient.getRecipientName(), recipient);
        }
    }

    @Override
    public Collection<IPFRecipient> getAllRecipients() {
        return recipients.values();
    }

    @Override
    public IPFRecipient getRecipientbyName(String name) {
        return recipients.get(name);
    }


}
