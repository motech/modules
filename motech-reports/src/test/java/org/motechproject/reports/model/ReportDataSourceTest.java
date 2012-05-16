package org.motechproject.reports.model;

import org.junit.Test;
import org.motechproject.reports.annotation.Report;
import org.motechproject.reports.annotation.ReportGroup;
import org.motechproject.reports.controller.sample.SampleData;
import org.motechproject.reports.controller.sample.SampleReportController;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;

public class ReportDataSourceTest {

    @Test
    public void isValidDataSourceIfAnnotatedWithReport() {
        assertTrue(ReportDataSource.isValidDataSource(SampleReportController.class));
    }

    @Test
    public void isNotValidDataSourceIfNotAnnotatedWithReport() {
        assertFalse(ReportDataSource.isValidDataSource(ReportDataSourceTest.class));
    }

    @Test
    public void nameIsSpecifiedInReportAnnotation() {
        assertEquals("sampleReports", new ReportDataSource(new SampleReportController()).name());
    }

    @Test
    public void shouldRetrieveDataFromDataSource() {
        assertArrayEquals(
                new SampleData[]{new SampleData("id1"), new SampleData("id2")},
                new ReportDataSource(new SampleReportController()).data("sampleReport", 1).toArray()
        );
    }

    @Test
    public void shouldRetrieveEmptyListWhenDataMethodIsNotSpecified() {
        assertEquals(0, new ReportDataSource(new WithoutDataMethod()).data("sampleReport", 1).size());
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionWhenDataMethodSpecifiedIsNotPublic() {
        assertEquals(0, new ReportDataSource(new WithPrivateDataMethod()).data("sampleReport", 1).size());
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionWhenDataMethodDoesNotHavePageNumber() {
        assertEquals(0, new ReportDataSource(new WithInvalidDataMethod()).data("sampleReport", 1).size());
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionWhenDataMethodDoesNotReturnAList() {
        assertEquals(0, new ReportDataSource(new WithInvalidReturnType()).data("sampleReport", 1).size());
    }

    @Test
    public void shouldReturnNameForTitle() {
        assertEquals("Without Data Method", new ReportDataSource(new WithoutDataMethod()).title());
    }

    @Test
    public void shouldReturnColumnHeaders() {
        assertEquals(asList("Id", "Custom column name"), new ReportDataSource(new ValidReportDataSource()).columnHeaders("sampleReport"));
    }

}

@ReportGroup(name = "validReportDataSource")
class ValidReportDataSource {

    @Report
    public List<SampleData> sampleReport() {
        return asList(new SampleData("id"));
    }
}


@ReportGroup(name = "withoutDataMethod")
class WithoutDataMethod {
}

@ReportGroup(name = "withPrivateDataMethod")
class WithPrivateDataMethod {

    @Report
    private List<SampleData> sampleReport(int pageNumber) {
        return asList(new SampleData("id"));
    }

}

@ReportGroup(name = "withInvalidDataMethod")
class WithInvalidDataMethod {

    @Report
    public List<SampleData> sampleReport() {
        return asList(new SampleData("id"));
    }

}

@ReportGroup(name = "withInvalidReturnType")
class WithInvalidReturnType {

    @Report
    public SampleData sampleReport() {
        return new SampleData("id");
    }
}

