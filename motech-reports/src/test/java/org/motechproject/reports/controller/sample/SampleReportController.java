package org.motechproject.reports.controller.sample;

import org.motechproject.reports.annotation.Report;
import org.motechproject.reports.annotation.ReportGroup;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

@Component
@ReportGroup(name = "sampleReports")
public class SampleReportController {

    private List<SampleData> sampleData1 = new ArrayList<SampleData>();
    private List<SampleData> sampleData2 = new ArrayList<SampleData>();
    public boolean isCalled = false;

    public SampleReportController() {
        this.sampleData1 = asList(new SampleData("id1"), new SampleData("id2"));
        this.sampleData2 = asList(new SampleData("id3"));
    }

    @Report
    public List<SampleData> sampleReport(int pageNumber) {
        isCalled = true;
        if (pageNumber == 1) {
            return sampleData1;
        } else if (pageNumber == 2) {
            return sampleData2;
        } else {
            return null;
        }
    }
}
