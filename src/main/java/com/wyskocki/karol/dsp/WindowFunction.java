package com.wyskocki.karol.dsp;

/**
 * Window function
 * <br/>More info: <a href="https://en.wikipedia.org/wiki/Window_function">Window_function</a>
 */
public interface WindowFunction {

    /**
     * Multiplies the input data using the window function.
     * @param data input data
     * @return data after windowing process
     */
    double[] processing(double[] data);

}
