package org.motechproject.reports.model;

import org.apache.commons.lang.StringUtils;
import org.motechproject.reports.annotation.ReportValue;

import java.lang.reflect.Method;

import static org.apache.commons.lang.StringUtils.*;

public class Column implements Comparable<Column> {

    private Method method;
    private Integer order;

    public Column(Method method) {
        this.method = method;
        order = method.getAnnotation(ReportValue.class).index();
    }

    public String name() {
        String name = nameFromAnnotation(method);
        if (name == null) {
            name = capitalize(method.getName().replace("get", ""));
            return join(splitByCharacterTypeCamelCase(name), " ");
        }
        return name;
    }

    @Override
    public int compareTo(Column o) {
        return this.order.compareTo(o.order);
    }

    public Method getMethod() {
        return method;
    }

    public String value(Object model) {
        method.setAccessible(true);
        try {
            return method.invoke(model).toString();
        } catch (Exception ignored) {
        }
        return "";
    }

    private String nameFromAnnotation(Method method) {
        ReportValue reportValue = method.getAnnotation(ReportValue.class);
        if (reportValue != null && StringUtils.isNotBlank(reportValue.column())) {
            return reportValue.column();
        }
        return null;
    }
}
