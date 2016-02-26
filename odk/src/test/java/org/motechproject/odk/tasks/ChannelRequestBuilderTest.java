package org.motechproject.odk.tasks;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.parser.impl.XformParserODK;
import org.motechproject.tasks.contract.ChannelRequest;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Version;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ChannelRequestBuilderTest {

    @Mock
    private BundleContext bundleContext;

    @Mock
    private Bundle bundle;

    @Mock
    private Version version;

    @Before
    public void setup() {
        when(bundleContext.getBundle()).thenReturn(bundle);
        when(bundle.getVersion()).thenReturn(version);
        when(bundle.getSymbolicName()).thenReturn("BundleSymbolicName");
        when(version.toString()).thenReturn("bundleVersion");
    }


    @Test
    public void testNestedRepeatGroups() throws Exception {
        Configuration configuration = new Configuration();
        configuration.setName("configName");

        File f = new File(getClass().getResource("/nested_repeat.xml").getFile());
        String xml = FileUtils.readFileToString(f);
        FormDefinition formDefinition = new XformParserODK().parse(xml, configuration.getName());
        List<FormDefinition> formDefinitions = new ArrayList<>();
        formDefinitions.add(formDefinition);

        ChannelRequestBuilder builder = new ChannelRequestBuilder(bundleContext, formDefinitions);
        ChannelRequest channelRequest = builder.build();
        assertEquals(channelRequest.getActionTaskEvents().size(), 2);
        assertEquals(channelRequest.getTriggerTaskEvents().size(), 4);

    }
}
