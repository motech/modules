package org.motechproject.server.alerts.contract;

import org.apache.commons.collections.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

import static org.apache.commons.beanutils.BeanUtils.initCause;
import static org.apache.commons.beanutils.PropertyUtils.getProperty;

/**
 * Checks if the given property of item inside a collection has the given value.
 *
 * @param <T> the type of object value.
 */
class Equal<T> implements Predicate {
    private static final Logger LOG = LoggerFactory.getLogger(Equal.class);

    private String propertyName;
    private T propertyValue;

    Equal(String propertyName, T propertyValue) {
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }

    public boolean evaluate(Object object) {
        boolean evaluation;

        try {
            evaluation = evaluateValue(propertyValue, (T) getProperty(object, propertyName));
        } catch (IllegalArgumentException e) {
            String errorMsg = "Problem during evaluation. Null value encountered in property path";

            IllegalArgumentException iae = new IllegalArgumentException(errorMsg, e);
            if (!initCause(iae, e)) {
                LOG.error(errorMsg, e);
            }
            throw iae;
        } catch (IllegalAccessException e) {
            String errorMsg = "Unable to access the property provided.";
            IllegalArgumentException iae = new IllegalArgumentException(errorMsg, e);
            if (!initCause(iae, e)) {
                LOG.error(errorMsg, e);
            }
            throw iae;
        } catch (InvocationTargetException e) {
            String errorMsg = "Exception occurred in property's getter";
            IllegalArgumentException iae = new IllegalArgumentException(errorMsg, e);
            if (!initCause(iae, e)) {
                LOG.error(errorMsg, e);
            }
            throw iae;
        } catch (NoSuchMethodException e) {
            String errorMsg = "Property not found.";
            IllegalArgumentException iae = new IllegalArgumentException(errorMsg, e);
            if (!initCause(iae, e)) {
                LOG.error(errorMsg, e);
            }
            throw iae;
        }

        return evaluation;
    }

    protected boolean evaluateValue(T expected, T actual) {
        return (expected == actual) || ((expected != null) && expected.equals(actual));
    }
}
