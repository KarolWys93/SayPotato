package com.wyskocki.karol.dsp;

public class DCT {

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

    static public double[] dct(double data[]){
        return dct(data, data.length);
    }

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

    static public double[] idct(double[] data){
        return idct(data, data.length);
    }

    public static void main(String[] args) {
        double[] dataTest = new double[128];

        for (int i = 0; i < dataTest.length; i++) {
            dataTest[i] = Math.sin(2*Math.PI*0.01*i*4) + Math.random()*0.4;
        }

        double[] dataAfterDCT = DCT.dct(dataTest, 64);
        double[] dataAfterIDCT = DCT.idct(dataAfterDCT, 128);

        boolean isOk = true;

        for (int i = 0; i < dataAfterIDCT.length; i++) {
            System.out.println(dataTest[i]+ " " + "." + " " + dataAfterIDCT[i]);
            if (Math.abs(dataTest[i] - dataAfterIDCT[i]) > 0.2)
                isOk = false;
        }

        System.out.println("Test ok: " + isOk);
    }

}
