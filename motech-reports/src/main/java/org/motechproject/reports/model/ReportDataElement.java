package org.motechproject.reports.model;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ReportDataElement {

    private final Columns columns;

    public ReportDataElement(Type returnType) {
        Type[] typeParameters = ((ParameterizedType) returnType).getActualTypeArguments();
        assertHasSingleTypeParameter(typeParameters);

        columns = new Columns((Class) typeParameters[0]);
    }

    private void assertHasSingleTypeParameter(Type[] typeParameters) {
        if (typeParameters.length != 1) {
            throw new RuntimeException("Return type should have only one generics type parameter");
        }
    }

    public List<String> columnHeaders() {
        List<String> columnHeaders = new ArrayList<String>();
        for (Column column : columns) {
            columnHeaders.add(column.name());
        }
        return columnHeaders;
    }

    public List<String> createRowData(Object model) {
        List<String> rowData = new ArrayList<String>();
        for (Column column : columns) {
            rowData.add(column.value(model));
        }
        return rowData;
    }

}
