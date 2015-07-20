package org.motechproject.batch.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A Spring MVC interceptor for handling incoming requests.
 * It responsible for setting the correct context classloader for
 * incoming requests.
 */
public class RequestLoggingInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(RequestLoggingInterceptor.class);

    private ClassLoader classLoader;
    private boolean bundleClassLoaderSet;

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) {
        LOGGER.info("preHandle Called");
        classLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(
                getClass().getClassLoader());
        bundleClassLoaderSet = true;
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
            HttpServletResponse response, Object handler, Exception ex) {
        LOGGER.info("afterCompletion Called");
        if (bundleClassLoaderSet) {
            Thread.currentThread().setContextClassLoader(classLoader);
        }
    }
}
