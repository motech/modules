package org.motechproject.reports.model;

import org.motechproject.reports.annotation.ReportValue;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang.StringUtils.*;

public class ReportDataElement {

    private final Class typeParameter;

    public ReportDataElement(Type returnType) {
        Type[] typeParameters = ((ParameterizedType) returnType).getActualTypeArguments();
        assertHasSingleTypeParameter(typeParameters);
        typeParameter = (Class) typeParameters[0];
    }

    private void assertHasSingleTypeParameter(Type[] typeParameters) {
        if (typeParameters.length != 1) {
            throw new RuntimeException("Return type should have only one generics type parameter");
        }
    }

    public List<String> columnHeaders() {
        List<String> columnHeaders = new ArrayList<String>();
        for (Method method : typeParameter.getDeclaredMethods()) {
            if (isValueMethod(method)) {
                String name = capitalize(method.getName().replace("get", ""));
                columnHeaders.add(join(splitByCharacterTypeCamelCase(name), " "));
            }
        }
        return columnHeaders;
    }

    public List<String> createRowData(Object model) {
        List<String> rowData = new ArrayList<String>();
        for (Method method : typeParameter.getDeclaredMethods()) {
            if (isValueMethod(method)) {
                method.setAccessible(true);
                validateValueMethod(method);
                try {
                    rowData.add(method.invoke(model).toString());
                } catch (Exception ignored) {
                }
            }
        }
        return rowData;
    }

    private void validateValueMethod(Method method) {
        if (method.getParameterTypes().length > 0) {
            throw new RuntimeException("value method should have no parameter");
        }
    }

    private boolean isValueMethod(Method method) {
        return method.isAnnotationPresent(ReportValue.class);
    }
}
