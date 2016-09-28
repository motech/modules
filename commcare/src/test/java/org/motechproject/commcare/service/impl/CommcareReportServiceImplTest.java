package org.motechproject.commcare.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.commcare.client.CommCareAPIHttpClient;
import org.motechproject.commcare.config.Config;
import org.motechproject.commcare.domain.report.ReportDataInfo;
import org.motechproject.commcare.service.CommcareConfigService;
import org.motechproject.commcare.util.ConfigsUtils;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Test class for {@link CommcareReportServiceImpl}
 */
public class CommcareReportServiceImplTest {

    private CommcareReportServiceImpl reportService;

    @Mock
    private CommCareAPIHttpClient commcareHttpClient;

    @Mock
    private CommcareConfigService configService;

    private Config config;

    @Before
    public void setUp() {
        initMocks(this);

        config = ConfigsUtils.prepareConfigOne();

        when(configService.getByName(null)).thenReturn(config);

        reportService = new CommcareReportServiceImpl(commcareHttpClient, configService);
    }

    @Test
    public void testReportByReportId() {
        String reportId = "testId";

        when(commcareHttpClient.singleReportDataRequest(config.getAccountConfig(), reportId)).thenReturn(individualReport());

        ReportDataInfo reportInstance = reportService.getReportByReportId(reportId);

        assertNotNull(reportInstance);

    }

    private String individualReport() {
        return "{" + "  \"columns\": [" + "    {" + "      \"header\": \"District\"," + "      \"slug\": \"district\"" + "    }," + "    {" + "      \"header\": \"Num Children Visited\"," + "      \"slug\": \"number_of_children_visited\"" + "    }," + "    {" + "      \"header\": \"Gender-male\"," + "      \"expand_column_value\": \"male\"," + "      \"slug\": \"gender-male\"" + "    }," + "    {" + "      \"header\": \"Gender-female\"," + "      \"expand_column_value\": \"female\"," + "      \"slug\": \"gender-female\"" + "    }" + "  ]," + "  \"data\": [" + "    {" + "      \"district\": \"Middlesex\"," + "      \"number_of_children_visited\": 46," + "      \"gender-male\": 10," + "      \"gender-female\": 35" + "    }," + "    {" + "      \"district\": \"Suffolk\"," + "      \"number_of_children_visited\": 85," + "      \"gender-male\": 81," + "      \"gender-female\": 4" + "    }" + "  ]," + "  \"next_page\": \"/a/motechproject/api/v0.5/configurablereportdata/9aab0eeb88555a7b4568676883e7379a/?offset=3&limit=3&state=vermont\"," + "  \"total_records\": 30" + "}";
    }
}
