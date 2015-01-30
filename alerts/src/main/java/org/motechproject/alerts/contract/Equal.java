package org.motechproject.alerts.contract;

import org.apache.commons.collections.Predicate;

import java.lang.reflect.InvocationTargetException;

import static org.apache.commons.beanutils.PropertyUtils.getProperty;

/**
 * Checks if the given property of item inside a collection has the given value.
 *
 * @param <T> the type of object value.
 */
class Equal<T> implements Predicate {

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
            throw new IllegalArgumentException("Problem during evaluation. Null value encountered in property path", e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Unable to access the property provided.", e);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException("Exception occurred in property's getter", e);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Property not found.", e);
        }

        return evaluation;
    }

    protected boolean evaluateValue(T expected, T actual) {
        return (expected == actual) || ((expected != null) && expected.equals(actual));
    }
}
