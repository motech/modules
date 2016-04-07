package org.motechproject.ihe.interop.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import org.motechproject.config.SettingsFacade;
import org.motechproject.ihe.interop.domain.HL7Recipient;
import org.motechproject.ihe.interop.service.HL7RecipientsService;
import org.motechproject.ihe.interop.util.Constants;
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

/**
 * Implementation of {@link HL7RecipientsService}. Recipients data is loaded from the
 * hl7-recipients.json configuration file.
 */
@Service("hl7RecipientsService")
public class HL7RecipientsServiceImpl implements HL7RecipientsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HL7RecipientsServiceImpl.class);

    @Autowired
    @Qualifier("iheSettings")
    private SettingsFacade settingsFacade;

    // TODO: save this to the database ?
    private Map<String, HL7Recipient> recipients;

    @PostConstruct
    public void init() {
        recipients = new HashMap<>();
        List<HL7Recipient> recipientsList;
        try (InputStream is = settingsFacade.getRawConfig(Constants.HL7_RECIPIENTS_FILE)) {
            String jsonText = IOUtils.toString(is);
            LOGGER.debug("Loading {}", Constants.HL7_RECIPIENTS_FILE);
            Gson gson = new Gson();
            recipientsList = gson.fromJson(jsonText, new TypeToken<List<HL7Recipient>>() { } .getType());
        } catch (Exception e) {
            String message = String.format("There seems to be a problem with the json text in %s: %s", Constants.HL7_RECIPIENTS_FILE,
                    e.getMessage());
            LOGGER.debug(message);
            throw new JsonIOException(message, e);
        }

        for (HL7Recipient recipient : recipientsList) {
            recipients.put(recipient.getRecipientName(), recipient);
        }
    }

    @Override
    public Collection<HL7Recipient> getAllRecipients() {
        return recipients.values();
    }

    @Override
    public HL7Recipient getRecipientbyName(String name) {
        return recipients.get(name);
    }
}
