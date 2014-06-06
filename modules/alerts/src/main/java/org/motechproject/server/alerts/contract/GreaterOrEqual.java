package org.motechproject.server.alerts.contract;

/**
 * Checks if the given number property of item inside a collection has value that is greater or
 * equal to the given number value.
 */
class GreaterOrEqual<T extends Number> extends Equal<T> {

    GreaterOrEqual(String propertyName, T propertyValue) {
        super(propertyName, propertyValue);
    }

    @Override
    protected boolean evaluateValue(T expected, T actual) {
        return Long.compare(expected.longValue(), actual.longValue()) <= 0;
    }
}
