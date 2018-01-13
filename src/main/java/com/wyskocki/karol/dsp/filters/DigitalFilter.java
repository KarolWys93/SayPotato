package com.wyskocki.karol.dsp.filters;

/**
 * Digital filter interface. It implements basics functions of any digital filter object.
 * Created by Karol on 03-06-2017.
 */
public interface DigitalFilter {

    /**
     * Gets input sample and return this sample after filtering.
     * @param sample
     * @return
     */
    public abstract double filter(double sample);

    /**
     * Gets array of samples and return these samples after filtering.
     * @param sample
     * @return
     */
    public abstract double[] filter(double[] sample);

    /**
     * Resets filter state.
     */
    public abstract void resetFilter();
}
