package org.motechproject.server.alerts.contract;

/**
 * Checks if the given number property of item inside a collection has value that is less or equal
 * to the given number value.
 */
class LessOrEqual<T extends Number> extends Equal<T> {

    LessOrEqual(String propertyName, T propertyValue) {
        super(propertyName, propertyValue);
    }

    @Override
    protected boolean evaluateValue(T expected, T actual) {
        return Long.compare(expected.longValue(), actual.longValue()) >= 0;
    }
}
