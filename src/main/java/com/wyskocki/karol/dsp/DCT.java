package com.wyskocki.karol.dsp;

/**
 * Discrete cosine transform
 * <br/>more info: <a href="https://en.wikipedia.org/wiki/Discrete_cosine_transform">DCT</a>
 */
public class DCT {

    /**
     * Calculates DCT
     * @param data signal
     * @param coefficientsNum number of DCT coefficients
     * @return result of DCT
     */
    static public double [] dct(double data[], int coefficientsNum){
        double[] g = new double[coefficientsNum];
        int N = data.length;
        double sum = 0;

        double A = Math.pow(2.0/(double)N, 0.5);

        for (int m = 0; m < data.length; m++) {
            sum += data[m];
        }
        g[0] = (Math.pow(1.0/(double)N, 0.5)) * sum;

        for (int k = 1; k < coefficientsNum; k++) {
            sum = 0;
            for (int m = 0; m < data.length; m++) {
                sum += data[m] * Math.cos((Math.PI*k*(2*m+1))/(2.0*N));
            }
            g[k] = A*sum;
        }
        return g;
    }

    /**
     * Calculates DCT. Number od DCT coefficients is equal to input data length.
     * @param data input data
     * @return result of DCT
     */
    static public double[] dct(double data[]){
        return dct(data, data.length);
    }

    /**
     * Calculates inverse DCT. This result is array. Size of this array is equal to dataLength parameter.
     * Data length parameter must by equal to length of data before DCT computation. Otherwise, result will be incorrect.
     * @param data input data
     * @param dataLength length of data after IDCT
     * @return data after IDCT
     */
    static public double[] idct(double data[], int dataLength){
        double[] g = new double[dataLength];
        double N = dataLength;
        double A = Math.pow(2.0/N, 0.5);
        int coefficientsNum = data.length;

        double g0 = (1/Math.pow(N, 0.5))*data[0];

        double sum;
        for (int m = 0; m < dataLength; m++) {
            sum = 0;
            for (int k = 1; k < coefficientsNum; k++) {
                sum += data[k] * Math.cos((Math.PI*k*(2*m)+1)/(2.0*N));
            }

            g[m] = g0 + A * sum;
        }
        return g;
    }

    /**
     * Calculates inverse DCT. Size of result array is the same as input array size.
     * @param data input array
     * @return data after IDCT
     */
    static public double[] idct(double[] data){
        return idct(data, data.length);
    }
}
