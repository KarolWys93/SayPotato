package com.wyskocki.karol.dsp.filters;

/**
 * Preemphasis filter. Causes gain of high frequencies, and damping low frequencies.
 * <br/> x<sub>n</sub>' = x<sub>n</sub> - alpha * x<sub>n-1</sub>
 * <br/> more info: <a href="https://en.wikipedia.org/wiki/Emphasis_(telecommunications)">preemphasis</a>
 */
public class Preemphasis implements DigitalFilter{

    double lastState = 0;
    double alpha = 0;

    /**
     * Creates preemphasis filter. Alpha = 0.97
     */
    public Preemphasis(){
        alpha = 0.97;
    }

    /**
     * Creates preemphsasis filter
     * @param alpha
     */
    public Preemphasis(double alpha){
        this.alpha = alpha;
    }

    @Override
    public double filter(double sample) {
        double result = sample - alpha * lastState;
        lastState = sample;
        return result;
    }

    @Override
    public double[] filter(double[] sample) {
        double[] result = new double[sample.length];
        for (int i =0; i<sample.length; i++){
            result[i] = filter(sample[i]);
        }
        return result;
    }

    @Override
    public void resetFilter() {
        lastState = 0;
    }
}
