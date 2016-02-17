package org.motechproject.ipf.osgi;

import org.apache.commons.lang.ArrayUtils;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.ipf.domain.IPFTemplate;
import org.motechproject.ipf.event.EventSubjects;
import org.motechproject.ipf.service.IPFRecipientsService;
import org.motechproject.ipf.service.IPFTemplateDataService;
import org.motechproject.ipf.util.Constants;
import org.motechproject.tasks.domain.ActionEvent;
import org.motechproject.tasks.domain.ActionParameter;
import org.motechproject.tasks.domain.Channel;
import org.motechproject.tasks.service.ChannelService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class IPFTaskServiceBundleIT extends BasePaxIT {

    private final Object waitLock = new Object();
    private static final String SAMPLE_TEMPLATE_NAME = "template_%d";
    private static final String SAMPLE_TEMPLATE = "<nodes><node>${template_%d_key_%d}</node><node>${template_%d_key_%d}</node><node>${template_%d_key_%d}</node><nodes>";
    private static final String SAMPLE_KEY = "template_%d_key_%d";
    private static final String SAMPLE_VALUE = "template_%d_value_%d";
    private static final String TEST_RECIPIENT_1 = "test_recipient_1";
    private static final String TEST_RECIPIENT_2 = "test_recipient_2";

    @Inject
    private ChannelService channelService;

    @Inject
    private IPFRecipientsService ipfRecipientsService;

    @Inject
    private IPFTemplateDataService ipfTemplateService;

    @After
    public void tearDown() {
        ipfTemplateService.deleteAll();
        channelService.unregisterChannel("org.motechproject.ipf");
    }

    @Test
    public void shouldUpdateTaskChannelAfterCreatingTemplate() throws InterruptedException {
        IPFTemplate ipfTemplate = getTemplate(1);
        verifyChannelNotExist();

        ipfTemplateService.create(ipfTemplate);
        //wait for channel registration
        wait2s();

        Channel channel = channelService.getChannel("org.motechproject.ipf");
        assertNotNull(channel);

        // 2 recipients and 1 template
        assertEquals(2, channel.getActionTaskEvents().size());
        assertEquals(0, channel.getTriggerTaskEvents().size());

        // name templateName.recipientName
        ActionEvent actionEvent = getActionByName(channel, String.format(SAMPLE_TEMPLATE_NAME, 1) + "." + TEST_RECIPIENT_1);
        assertNotNull(actionEvent);
        assertEquals(String.format(SAMPLE_TEMPLATE_NAME, 1) + " " + TEST_RECIPIENT_1, actionEvent.getDisplayName());
        assertEquals(EventSubjects.TEMPLATE_ACTION + "." + String.format(SAMPLE_TEMPLATE_NAME, 1) + "." + TEST_RECIPIENT_1, actionEvent.getSubject());

        Set<ActionParameter> params = actionEvent.getActionParameters();
        assertNotNull(params);
        assertEquals(5, params.size());

        List<ActionParameter> paramsList = new ArrayList<>(params);
        assertParameter(paramsList.get(0), null, String.format(SAMPLE_KEY, 1, 1), String.format(SAMPLE_VALUE, 1, 1), false, true);
        assertParameter(paramsList.get(1), null, String.format(SAMPLE_KEY, 1, 2), String.format(SAMPLE_VALUE, 1, 2), false, true);
        assertParameter(paramsList.get(2), null, String.format(SAMPLE_KEY, 1, 3), String.format(SAMPLE_VALUE, 1, 3), false, true);
        assertParameter(paramsList.get(3), TEST_RECIPIENT_1, Constants.RECIPIENT_NAME_PARAM, Constants.RECIPIENT_DISPLAY_NAME, true, true);
        assertParameter(paramsList.get(4), String.format(SAMPLE_TEMPLATE_NAME, 1), Constants.TEMPLATE_NAME_PARAM, Constants.TEMPLATE_DISPLAY_NAME, true, true);

        actionEvent = getActionByName(channel, String.format(SAMPLE_TEMPLATE_NAME, 1) + "." + TEST_RECIPIENT_2);
        assertNotNull(actionEvent);
        assertEquals(String.format(SAMPLE_TEMPLATE_NAME, 1) + " " + TEST_RECIPIENT_2, actionEvent.getDisplayName());
        assertEquals(EventSubjects.TEMPLATE_ACTION + "." + String.format(SAMPLE_TEMPLATE_NAME, 1) + "." + TEST_RECIPIENT_2, actionEvent.getSubject());

        params = actionEvent.getActionParameters();
        assertNotNull(params);
        assertEquals(5, params.size());

        paramsList = new ArrayList<>(params);
        assertParameter(paramsList.get(0), null, String.format(SAMPLE_KEY, 1, 1), String.format(SAMPLE_VALUE, 1, 1), false, true);
        assertParameter(paramsList.get(1), null, String.format(SAMPLE_KEY, 1, 2), String.format(SAMPLE_VALUE, 1, 2), false, true);
        assertParameter(paramsList.get(2), null, String.format(SAMPLE_KEY, 1, 3), String.format(SAMPLE_VALUE, 1, 3), false, true);
        assertParameter(paramsList.get(3), TEST_RECIPIENT_2, Constants.RECIPIENT_NAME_PARAM, Constants.RECIPIENT_DISPLAY_NAME, true, true);
        assertParameter(paramsList.get(4), String.format(SAMPLE_TEMPLATE_NAME, 1), Constants.TEMPLATE_NAME_PARAM, Constants.TEMPLATE_DISPLAY_NAME, true, true);
    }

    @Test
    public void shouldUpdateTaskChannelAfterUpdatingTemplate() throws InterruptedException {
        IPFTemplate ipfTemplate1 = getTemplate(2);
        IPFTemplate ipfTemplate2 = getTemplate(3);
        verifyChannelNotExist();

        ipfTemplate1 = ipfTemplateService.create(ipfTemplate1);
        ipfTemplate2 = ipfTemplateService.create(ipfTemplate2);
        wait2s();

        Channel channel = channelService.getChannel("org.motechproject.ipf");
        assertNotNull(channel);

        // 2 recipients and 2 templates
        assertEquals(4, channel.getActionTaskEvents().size());
        assertEquals(0, channel.getTriggerTaskEvents().size());

        ipfTemplate2.setTemplateName("new_template_name");
        ipfTemplate2.getProperties().remove(String.format(SAMPLE_KEY, 3, 3));
        ipfTemplateService.update(ipfTemplate2);
        wait2s();

        channel = channelService.getChannel("org.motechproject.ipf");
        assertNotNull(channel);

        ActionEvent actionEvent = getActionByName(channel, "new_template_name" + "." + TEST_RECIPIENT_1);
        assertNotNull(actionEvent);
        assertEquals("new_template_name" + " " + TEST_RECIPIENT_1, actionEvent.getDisplayName());
        assertEquals(EventSubjects.TEMPLATE_ACTION + "." + "new_template_name." + TEST_RECIPIENT_1, actionEvent.getSubject());

        Set<ActionParameter> params = actionEvent.getActionParameters();
        assertNotNull(params);
        assertEquals(4, params.size());

        List<ActionParameter> paramsList = new ArrayList<>(params);
        assertParameter(paramsList.get(0), null, String.format(SAMPLE_KEY, 3, 2), String.format(SAMPLE_VALUE, 3, 2), false, true);
        assertParameter(paramsList.get(1), null, String.format(SAMPLE_KEY, 3, 1), String.format(SAMPLE_VALUE, 3, 1), false, true);
        assertParameter(paramsList.get(2), TEST_RECIPIENT_1, Constants.RECIPIENT_NAME_PARAM, Constants.RECIPIENT_DISPLAY_NAME, true, true);
        assertParameter(paramsList.get(3), "new_template_name", Constants.TEMPLATE_NAME_PARAM, Constants.TEMPLATE_DISPLAY_NAME, true, true);
    }


    @Test
    public void shouldUpdateTaskChannelAfterDeletingTemplate() throws InterruptedException {
        IPFTemplate ipfTemplate1 = getTemplate(4);
        IPFTemplate ipfTemplate2 = getTemplate(5);
        verifyChannelNotExist();

        ipfTemplate1 = ipfTemplateService.create(ipfTemplate1);
        ipfTemplate2 = ipfTemplateService.create(ipfTemplate2);
        wait2s();

        Channel channel = channelService.getChannel("org.motechproject.ipf");
        assertNotNull(channel);

        // 2 recipients and 2 templates
        assertEquals(4, channel.getActionTaskEvents().size());
        assertEquals(0, channel.getTriggerTaskEvents().size());

        ipfTemplateService.delete(ipfTemplate1);
        wait2s();

        channel = channelService.getChannel("org.motechproject.ipf");
        assertNotNull(channel);

        // 2 recipients and 1 template
        assertEquals(2, channel.getActionTaskEvents().size());
        assertEquals(0, channel.getTriggerTaskEvents().size());
    }

    @Test
    public void shouldRemoveTaskChannel() throws InterruptedException {
        IPFTemplate ipfTemplate = getTemplate(6);
        verifyChannelNotExist();

        ipfTemplate = ipfTemplateService.create(ipfTemplate);
        //wait for channel registration
        wait2s();

        Channel channel = channelService.getChannel("org.motechproject.ipf");
        assertNotNull(channel);

        ipfTemplateService.delete(ipfTemplate);
        wait2s();

        verifyChannelNotExist();
    }

    private void assertParameter(ActionParameter param, String value, String key, String displayName, boolean hidden, boolean required) {
        assertEquals(value, param.getValue());
        assertEquals(key, param.getKey());
        assertEquals(displayName, param.getDisplayName());
        assertEquals("UNICODE", param.getType().getValue());
        assertEquals(hidden, param.isHidden());
        assertEquals(required, param.isRequired());
    }

    private ActionEvent getActionByName(Channel channel, String name) {
        for (ActionEvent actionEvent : channel.getActionTaskEvents()) {
            if (actionEvent.getName().equals(name))  {
                return actionEvent;
            }
        }
        return null;
    }

    private IPFTemplate getTemplate(Integer num) {
        Map<String, String> map = new HashMap<>();
        map.put(String.format(SAMPLE_KEY, num, 1), String.format(SAMPLE_VALUE, num, 1));
        map.put(String.format(SAMPLE_KEY, num, 2), String.format(SAMPLE_VALUE, num, 2));
        map.put(String.format(SAMPLE_KEY, num, 3), String.format(SAMPLE_VALUE, num, 3));

        return new IPFTemplate(String.format(SAMPLE_TEMPLATE_NAME, num), ArrayUtils.toObject(String.format(SAMPLE_TEMPLATE, num, 1, num, 2, num, 3).getBytes()), map);
    }

    private void verifyChannelNotExist() {
        Channel channel = channelService.getChannel("org.motechproject.ipf");
        assertNull(channel);
    }

    private void wait2s() throws InterruptedException {
        synchronized (waitLock) {
            waitLock.wait(2000);
        }
    }
}
