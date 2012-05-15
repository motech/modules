package org.motechproject.reports.model;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Repository;

import java.util.Hashtable;
import java.util.Map;

import static org.motechproject.reports.model.ReportDataSource.isValidDataSource;

@Repository
public class AllReportDataSources implements BeanPostProcessor {

    private Map<String, ReportDataSource> reportDataSources = new Hashtable<String, ReportDataSource>();

    public ReportDataSource get(String path) {
        return reportDataSources.get(path);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (isValidDataSource(bean.getClass())) {
            ReportDataSource dataSource = new ReportDataSource(bean);
            reportDataSources.put(dataSource.name(), dataSource);
        }
        return bean;
    }
}
