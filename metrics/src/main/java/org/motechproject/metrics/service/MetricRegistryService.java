package org.motechproject.metrics.service;

import org.motechproject.metrics.api.Counter;
import org.motechproject.metrics.api.Gauge;
import org.motechproject.metrics.api.Histogram;
import org.motechproject.metrics.api.Meter;
import org.motechproject.metrics.api.Timer;

import java.util.function.Supplier;

/**
 * Represents a registry that creates and/or retrieves a variety of metric types and allows registered metrics to be
 * enabled or disabled.
 */
public interface MetricRegistryService {
    /**
     * Sets all registered metrics to enabled or disabled depending on the value of the enabled parameter.
     *
     * @param enabled If true, sets all metrics to enabled; sets all metrics to disabled otherwise.
     */
    void setEnabled(boolean enabled);

    /**
     * Get the counter associated with the given name.
     *
     * @param name the name of the counter
     * @return the counter associated with the given name
     */
    Counter counter(final String name);

    /**
     * Get the histogram associated with the given name.
     *
     * @param name the name of the histogram
     * @return the histogram associated with the given name
     */
    Histogram histogram(final String name);

    /**
     * Get the meter associated with the given name.
     *
     * @param name the name of the meter
     * @return the meter associated with the given name
     */
    Meter meter(final String name);

    /**
     * Get the timer associated with the given name.
     *
     * @param name the name of the meter
     * @return the meter associated with the given name
     */
    Timer timer(final String name);

    /**
     * Register an implementation of the gauge interface.
     *
     * @param name the name of the gauge
     * @param gauge the implementation of the gauge interface
     * @param <T> the type of the gauge's return value
     * @return the registered gauge
     */
    <T> Gauge<T> registerGauge(final String name, final Gauge<T> gauge);

    /**
     * Register a ratio gauge.
     *
     * @param name the name of the gauge
     * @param numerator a function returning a number represents the value of the numerator
     * @param denominator a function returning a number that represents the value of the denominator
     * @param <T> a type of number
     * @return the registered gauge
     */
    <T extends Number> Gauge<Double> registerRatioGauge(final String name, Supplier<T> numerator, Supplier<T> denominator);

    /**
     * Returns whether a metric is registered or not by the given metric name.
     *
     * @param name the name of the metric
     * @return true if registered, false otherwise
     */
    boolean isRegistered(String name);
}
