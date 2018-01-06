package com.wyskocki.karol.dsp;

public class HammingWindow implements WindowFunction{

    private double alpha = 0;
    private double beta = 0;

    public HammingWindow(double alpha){
        this.alpha = alpha;
        this.beta = 1 - alpha;
    }

    public HammingWindow(){
        this.alpha = 0.54;
        this.beta = 1 - alpha;
    }


    @Override
    public double[] processing(double[] data) {
        double[] result = new double[data.length];

        for (int i = 0; i<data.length; i++){
            double window = alpha - beta*Math.cos((2*Math.PI*i)/(data.length-1));
            result [i] = data[i]*window;
        }
        return result;
    }
}
