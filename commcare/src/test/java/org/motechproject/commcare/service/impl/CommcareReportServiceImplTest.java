package org.motechproject.commcare.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.commcare.client.CommCareAPIHttpClient;
import org.motechproject.commcare.config.Config;
import org.motechproject.commcare.domain.report.ReportsMetadataInfo;
import org.motechproject.commcare.service.CommcareConfigService;
import org.motechproject.commcare.util.ConfigsUtils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class CommcareReportServiceImplTest {

    private static final String REPORTS_LIST_RESPONSE = "{\"meta\":{\"total_count\":2},\"objects\":[{\"columns\":[{\"column_id\":\"name\",\"display\":\"Name\",\"type\":\"field\"},{\"column_id\":\"gender\",\"display\":\"Gender\",\"type\":\"expanded\"},{\"column_id\":\"address\",\"display\":\"Person Address\",\"type\":\"field\"}],\"filters\":[{\"datatype\":\"string\",\"slug\":\"closed\",\"type\":\"choice_list\"},{\"datatype\":\"string\",\"slug\":\"owner_name\",\"type\":\"choice_list\"}],\"title\":\"Test Report 1\",\"id\":\"9aab0eeb88555a7b3bc8676883e7379a\",\"resource_uri\":\"/a/domainOne/api/v0.5/simplereportconfiguration/9aab0eeb88555a7b3bc8676883e7379a/\"},{\"columns\":[{\"column_id\":\"district\",\"display\":\"District\",\"type\":\"field\"},{\"column_id\":\"number_of_children_visited\",\"display\":\"Num Children Visited\",\"type\":\"field\"},{\"column_id\":\"number_of_children_underweight\",\"display\":\"Underweight\",\"type\":\"field\"}],\"filters\":[{\"datatype\":\"string\",\"slug\":\"closed\",\"type\":\"choice_list\"},{\"datatype\":\"string\",\"slug\":\"owner_name\",\"type\":\"choice_list\"},{\"datatype\":\"integer\",\"slug\":\"child_age\",\"type\":\"dynamic_choice_list\"},{\"datatype\":\"string\",\"slug\":\"form_date\",\"type\":\"date\"}],\"title\":\"Test Report 2\",\"id\":\"9aab0eeb88555a7b4568676883e7379a\",\"resource_uri\":\"/a/domainOne/api/v0.5/simplereportconfiguration/9aab0eeb88555a7b4568676883e7379a/\"}]}";
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
    public void shouldGetReports() {
        when(commcareHttpClient.reportsListMetadataRequest(configService.getByName(null).getAccountConfig())).thenReturn(getResponseForReportsList());

        ReportsMetadataInfo reportsMetadataInfo = reportService.getReportsList();

        assertThat(reportsMetadataInfo.getMetadataInfo().getTotalCount(), equalTo(2));
        assertThat(reportsMetadataInfo.getReportMetadataInfoList().size(), equalTo(2));
        assertThat(reportsMetadataInfo.getReportMetadataInfoList().get(0).getColumns().size(), equalTo(3));
        assertThat(reportsMetadataInfo.getReportMetadataInfoList().get(0).getFilters().size(), equalTo(2));
    }

    private String getResponseForReportsList() {
        return REPORTS_LIST_RESPONSE;
    }
}