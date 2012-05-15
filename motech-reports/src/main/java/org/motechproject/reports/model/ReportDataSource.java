package org.motechproject.reports.model;

import org.motechproject.reports.annotation.Report;
import org.motechproject.reports.annotation.ReportData;
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
        return controller.getClass().getAnnotation(Report.class).name();
    }

    public String title() {
        return join(splitByCharacterTypeCamelCase(capitalize(name())), " ");
    }

    public static boolean isValidDataSource(Class<?> beanClass) {
        return beanClass.isAnnotationPresent(Report.class);
    }

    public List<Object> data(int pageNumber) {
        try {
            Method method = getDataMethod();
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

    public List<String> columnHeaders() {
        return new ReportDataElement(getDataMethod().getGenericReturnType()).columnHeaders();
    }

    public List<String> createRowData(Object model) {
        return new ReportDataElement(getDataMethod().getGenericReturnType()).createRowData(model);
    }

    private Method getDataMethod() {
        for (Method method : controller.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(ReportData.class)) {
                return method;
            }
        }
        return null;
    }
}
