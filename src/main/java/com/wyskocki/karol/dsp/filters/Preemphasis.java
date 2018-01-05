package com.wyskocki.karol.dsp.filters;

public class Preemphasis implements DigitalFilter{

    double lastState = 0;
    double alpha = 0;

    public Preemphasis(){
        alpha = 0.97;
    }

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
