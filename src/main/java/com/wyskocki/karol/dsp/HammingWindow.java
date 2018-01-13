package com.wyskocki.karol.dsp;

/**
 * Hamming Window
 * <br/>w(n) = alpha - beta * cos((2*PI*n)/N-1)
 * <br/> beta = 1 - alpha
 * <br/>more info: <a href="https://en.wikipedia.org/wiki/Window_function#Hamming_window">Window_function</a>
 */
public class HammingWindow implements WindowFunction{

    private double alpha = 0;
    private double beta = 0;

    /**
     * Creates Hamming window function
     * @param alpha alpha
     */
    public HammingWindow(double alpha){
        this.alpha = alpha;
        this.beta = 1 - alpha;
    }

    /**
     * Creates Hamming window function
     * Alpha is 0.54
     */
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
