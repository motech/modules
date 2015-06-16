package org.motechproject.commcare.tasks.builder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.commcare.config.Config;
import org.motechproject.commcare.config.Configs;
import org.motechproject.commcare.service.CommcareConfigService;

import java.util.Collections;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CommcareDataProviderBuilderTest {

    @InjectMocks
    private CommcareDataProviderBuilder builder = new CommcareDataProviderBuilder();

    @Mock
    private CommcareConfigService configService;

    @Test
    public void shouldReturnEmptyStringForNoConfigurations() {
        when(configService.getConfigs()).thenReturn(new Configs(Collections.<Config>emptyList()));

        String providerBody = builder.generateDataProvider();

        assertNull(providerBody);
    }
}
