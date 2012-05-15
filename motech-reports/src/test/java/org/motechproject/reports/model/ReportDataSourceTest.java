package org.motechproject.reports.model;

import org.junit.Test;
import org.motechproject.reports.annotation.Report;
import org.motechproject.reports.annotation.ReportData;
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
        assertEquals("sampleReport", new ReportDataSource(new SampleReportController()).name());
    }

    @Test
    public void shouldRetrieveDataFromDataSource() {
        assertArrayEquals(
                new SampleData[]{new SampleData("id1"), new SampleData("id2")},
                new ReportDataSource(new SampleReportController()).data(1).toArray()
        );
    }

    @Test
    public void shouldRetrieveEmptyListWhenDataMethodIsNotSpecified() {
        assertEquals(0, new ReportDataSource(new WithoutDataMethod()).data(1).size());
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionWhenDataMethodSpecifiedIsNotPublic() {
        assertEquals(0, new ReportDataSource(new WithPrivateDataMethod()).data(1).size());
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionWhenDataMethodDoesNotHavePageNumber() {
        assertEquals(0, new ReportDataSource(new WithInvalidDataMethod()).data(1).size());
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionWhenDataMethodDoesNotReturnAList() {
        assertEquals(0, new ReportDataSource(new WithInvalidReturnType()).data(1).size());
    }

    @Test
    public void shouldReturnNameForTitle() {
        assertEquals("Without Data Method", new ReportDataSource(new WithoutDataMethod()).title());
    }

    @Test
    public void shouldReturnColumnHeaders() {
        assertEquals(asList("Id", "Custom column name"), new ReportDataSource(new ValidReportDataSource()).columnHeaders());
    }

}

@Report(name = "validReportDataSource")
class ValidReportDataSource {

    @ReportData
    public List<SampleData> data() {
        return asList(new SampleData("id"));
    }
}


@Report(name = "withoutDataMethod")
class WithoutDataMethod {
}

@Report(name = "withPrivateDataMethod")
class WithPrivateDataMethod {

    @ReportData
    private List<SampleData> data(int pageNumber) {
        return asList(new SampleData("id"));
    }

}

@Report(name = "withInvalidDataMethod")
class WithInvalidDataMethod {

    @ReportData
    public List<SampleData> data() {
        return asList(new SampleData("id"));
    }

}

@Report(name = "withInvalidReturnType")
class WithInvalidReturnType {

    @ReportData
    public SampleData data() {
        return new SampleData("id");
    }
}

