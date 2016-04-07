package org.motechproject.metrics.model;

import org.motechproject.metrics.api.Gauge;

/**
 * An implementation of a Gauge.
 *
 * @param <T> the return type when getting the gauge's value
 */
public class GaugeAdapter<T> implements Gauge<T> {
    private final com.codahale.metrics.Gauge<T> gauge;

    public GaugeAdapter(com.codahale.metrics.Gauge<T> gauge) {
        this.gauge = gauge;
    }

    /**
     * Get the gauge's value.
     *
     * @return the gauge's value.
     */
    @Override
    public T getValue() {
        return gauge.getValue();
    }
}
