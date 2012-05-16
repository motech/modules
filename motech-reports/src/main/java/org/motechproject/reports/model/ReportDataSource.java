package org.motechproject.reports.model;

import org.motechproject.reports.annotation.Report;
import org.motechproject.reports.annotation.ReportGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang.StringUtils.*;

public class ReportDataSource {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Object controller;

    public ReportDataSource(Object controller) {
        this.controller = controller;
    }

    public String name() {
        return controller.getClass().getAnnotation(ReportGroup.class).name();
    }

    public String title() {
        return join(splitByCharacterTypeCamelCase(capitalize(name())), " ");
    }

    public static boolean isValidDataSource(Class<?> beanClass) {
        return beanClass.isAnnotationPresent(ReportGroup.class);
    }

    public List<Object> data(String reportName, int pageNumber) {
        try {
            Method method = getDataMethod(reportName);
            if (method != null) {
                return (List<Object>) method.invoke(controller, pageNumber);
            }
        } catch (IllegalAccessException e) {
            logger.error("Data method should be public" + e.getMessage());
            throw new RuntimeException("Data method should be public" + e.getMessage());
        } catch (InvocationTargetException e) {
            logger.error("Format of data method is invalid." + e.getMessage());
            throw new RuntimeException("Format of data method is invalid." + e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("Format of data method is invalid." + e.getMessage());
            throw new RuntimeException("Format of data method is invalid." + e.getMessage());
        } catch (ClassCastException e) {
            logger.error("Format of data method is invalid." + e.getMessage());
            throw new RuntimeException("Format of data method is invalid." + e.getMessage());
        }
        return new ArrayList<Object>();
    }

    public List<String> columnHeaders(String reportName) {
        return new ReportDataModel(getDataMethod(reportName).getGenericReturnType()).columnHeaders();
    }

    public List<String> rowData(String reportName, Object model) {
        return new ReportDataModel(getDataMethod(reportName).getGenericReturnType()).rowData(model);
    }

    private Method getDataMethod(String reportName) {
        for (Method method : controller.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Report.class) && method.getName().equalsIgnoreCase(reportName)) {
                return method;
            }
        }
        return null;
    }
}
