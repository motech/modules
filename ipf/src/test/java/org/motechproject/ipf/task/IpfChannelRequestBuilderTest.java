package org.motechproject.ipf.task;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.ipf.domain.IPFRecipient;
import org.motechproject.ipf.domain.IPFTemplate;
import org.motechproject.ipf.event.EventSubjects;
import org.motechproject.ipf.util.Constants;
import org.motechproject.tasks.contract.ActionEventRequest;
import org.motechproject.tasks.contract.ActionParameterRequest;
import org.motechproject.tasks.contract.ChannelRequest;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Version;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class IpfChannelRequestBuilderTest {

    @Mock
    private Bundle bundle;

    @Mock
    private Version version;

    @Mock
    private BundleContext bundleContext;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldBuildChannel() {
        Map<String, String> params = new HashMap<>();
        params.put("one", "One");
        IPFTemplate ipfTemplate1 = new IPFTemplate("template_name_1", ArrayUtils.toObject("template_data_1".getBytes()), params);
        List<IPFTemplate> templates = new ArrayList<>();
        templates.add(ipfTemplate1);

        params = new HashMap<>();
        params.put("two", "Two");
        params.put("three", "Three");
        IPFTemplate ipfTemplate2 = new IPFTemplate("template_name_2", ArrayUtils.toObject("template_data_2".getBytes()), params);
        templates.add(ipfTemplate2);

        Collection<IPFRecipient> recipients = new ArrayList<>();
        IPFRecipient ipfRecipient = new IPFRecipient("recipient_1", "url_1");
        recipients.add(ipfRecipient);

        when(bundleContext.getBundle()).thenReturn(bundle);
        when(bundle.getSymbolicName()).thenReturn("org.motechproject.ipf");
        when(bundle.getVersion()).thenReturn(version);
        when(version.toString()).thenReturn("1.0.0");

        IpfChannelRequestBuilder ipfChannelRequestBuilder = new IpfChannelRequestBuilder(bundleContext, templates, recipients);
        ChannelRequest channelRequest = ipfChannelRequestBuilder.build();

        assertNotNull(channelRequest);
        assertEquals("org.motechproject.ipf", channelRequest.getModuleName());
        assertEquals("1.0.0", channelRequest.getModuleVersion());
        assertEquals(Constants.CHANNEL_DISPLAY_NAME, channelRequest.getDisplayName());

        assertEquals(0, channelRequest.getTriggerTaskEvents().size());
        assertEquals(2, channelRequest.getActionTaskEvents().size());

        ActionEventRequest actionEventRequest = channelRequest.getActionTaskEvents().get(0);
        assertEquals("template_name_1 recipient_1" ,actionEventRequest.getDisplayName());
        assertEquals(EventSubjects.TEMPLATE_ACTION + ".template_name_1.recipient_1" ,actionEventRequest.getSubject());
        assertEquals("template_name_1.recipient_1" ,actionEventRequest.getName());

        List<ActionParameterRequest> paramsList = new ArrayList<>(actionEventRequest.getActionParameters());
        assertParameter(paramsList.get(0), null, "one", "One", false, true);
        assertParameter(paramsList.get(1), "recipient_1", Constants.RECIPIENT_NAME_PARAM, Constants.RECIPIENT_DISPLAY_NAME, true, true);
        assertParameter(paramsList.get(2), "template_name_1", Constants.TEMPLATE_NAME_PARAM, Constants.TEMPLATE_DISPLAY_NAME, true, true);

        actionEventRequest = channelRequest.getActionTaskEvents().get(1);
        assertEquals("template_name_2 recipient_1" ,actionEventRequest.getDisplayName());
        assertEquals(EventSubjects.TEMPLATE_ACTION + ".template_name_2.recipient_1" ,actionEventRequest.getSubject());
        assertEquals("template_name_2.recipient_1" ,actionEventRequest.getName());

        paramsList = new ArrayList<>(actionEventRequest.getActionParameters());
        assertParameter(paramsList.get(0), null, "two", "Two", false, true);
        assertParameter(paramsList.get(1), null, "three", "Three", false, true);
        assertParameter(paramsList.get(2), "recipient_1", Constants.RECIPIENT_NAME_PARAM, Constants.RECIPIENT_DISPLAY_NAME, true, true);
        assertParameter(paramsList.get(3), "template_name_2", Constants.TEMPLATE_NAME_PARAM, Constants.TEMPLATE_DISPLAY_NAME, true, true);
    }

    private void assertParameter(ActionParameterRequest param, String value, String key, String displayName, boolean hidden, boolean required) {
        assertEquals(value, param.getValue());
        assertEquals(key, param.getKey());
        assertEquals(displayName, param.getDisplayName());
        assertEquals("UNICODE", param.getType());
        assertEquals(hidden, param.isHidden());
        assertEquals(required, param.isRequired());
    }
}
