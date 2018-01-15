package sayPotato;

public class Utils {

    public static double standardDeviation(double[] values){
        double mean = Utils.meanValue(values);

        double sum = 0;

        for (int i = 0; i < values.length; i++) {
            sum += Math.pow((values[i] - mean),2);
        }
        return Math.pow(sum/(values.length-1), 0.5);
    }

    public static double getMaxValue(double [] values){
        double maxV = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < values.length; i++) {
            if (values[i] > maxV){
                maxV = values[i];
            }
        }
        return maxV;
    }

    public static double getMinValue(double [] values){
        double minV = Double.MAX_VALUE;
        for (int i = 0; i < values.length; i++) {
            if (values[i] < minV){
                minV = values[i];
            }
        }
        return minV;
    }

    public static double meanValue(double[] values){
        double sum = 0;
        for (int i = 0; i < values.length; i++) {
            sum+=values[i];
        }
        return sum/values.length;
    }
}
