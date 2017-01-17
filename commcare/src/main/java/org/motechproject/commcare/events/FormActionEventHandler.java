package org.motechproject.commcare.events;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.motechproject.commcare.domain.FormValueElement;
import org.motechproject.commcare.domain.FormXml;
import org.motechproject.commcare.domain.MetadataValue;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.commcare.service.CommcareFormService;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * This class serves as the event handler for the task actions, exposed by the Commcare module.
 * Respective methods extract the necessary data from the {@link MotechEvent} instance and
 * pass them to the {@link CommcareFormService} that handles all the operations on Commcare forms.
 */
@Component
public class FormActionEventHandler {

    private static final String INSTANCE_ID_KEY = "instanceID";
    private static final String USERNAME_KEY = "username";
    private static final String TIME_START_KEY = "timeStart";
    private static final String TIME_END_KEY = "timeEnd";

    private static final String USERNAME_VALUE = "MOTECH";

    private static final DateTimeFormatter COMMCARE_DATETIME_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    @Autowired
    private CommcareFormService commcareFormService;

    /**
     * Handles events, connected with sending Commcare forms. The event subject should have the following syntax:
     * {@code EventSubjects.SUBMIT_FORM.[form XMLNS].[config name]}
     *
     * @param event the event, containing parameters necessary to send the Commcare form
     */
    @MotechListener(subjects = EventSubjects.SUBMIT_FORM + ".*")
    public void submitForm(MotechEvent event) {
        String configName = EventSubjects.getConfigName(event.getSubject());
        Map<String, Object> parameters = event.getParameters();

        FormXml formXml = parseEventParametersToFormXml(parameters);

        Map<String, MetadataValue> formMetadata = new HashMap<>();
        formMetadata.put(INSTANCE_ID_KEY, new MetadataValue(UUID.randomUUID().toString()));
        formMetadata.put(USERNAME_KEY, new MetadataValue(USERNAME_VALUE));
        formMetadata.put(TIME_START_KEY, new MetadataValue(new DateTime().toString(COMMCARE_DATETIME_FORMAT)));
        formMetadata.put(TIME_END_KEY, new MetadataValue(new DateTime().toString(COMMCARE_DATETIME_FORMAT)));

        formXml.setMetadata(formMetadata);

        commcareFormService.uploadForm(formXml, configName);
    }

    private FormXml parseEventParametersToFormXml(Map<String, Object> parameters) {
        FormXml formXml = new FormXml();

        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            if (entry.getValue() != null) {
                if ("xmlns".equals(entry.getKey())) {
                    formXml.setXmlns(entry.getValue().toString());
                } else {
                    List<String> xmlPath = discoverXmlPath(entry.getKey());
                    FormValueElement element = formXml.getElementByPath(xmlPath);
                    element.setValue(entry.getValue().toString());
                }
            }
        }

        return formXml;
    }

    private List<String> discoverXmlPath(String key) {
        String[] xmlTree = key.split("/");
        // First elements are always empty string and "data" and are of no interest to us
        return Arrays.asList(xmlTree).subList(2, xmlTree.length);
    }

}
