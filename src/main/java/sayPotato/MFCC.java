package sayPotato;

import com.wyskocki.karol.dsp.DCT;
import com.wyskocki.karol.dsp.Spectrum;

import java.util.ArrayList;


/**
 * MFCC class calculates and contains MFC coefficients for given signal spectrum
 */
public class MFCC {
    private int coeffsNum;
    private int filtersNum;
    double[] coefficients;

    /**
     * Create MFCC object.
     * @param coeffsNum number of MFCC created from signal spectrum
     * @param filtersNum number of mel-scale filters using to processing
     */
    public MFCC(int coeffsNum, int filtersNum){
        this.coeffsNum = coeffsNum;
        this.filtersNum = filtersNum;
    }

    /**
     * Calculates MFC coefficients (MFCC) form spectrum.
     * @param spectrum signal spectrum
     */
    public void calculate(Spectrum spectrum){

        ArrayList<TriangleMelFilter> filters;
        filters = createFilters(TriangleMelFilter.class, spectrum.getSpectrumWidth(), filtersNum);
        double[] filterResult = melScaleFiltering(spectrum, filters);
        coefficients = DCT.dct(log(filterResult), coeffsNum);
    }

    /**
     * Returns the base 10 logarithm array of input array.
     * @param values - array of double
     * @return - the base 10 logarithm of values array
     */
    private double[] log(double[] values){
        double[] result = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = Math.log10(values[i]);
        }
        return result;
    }

    /**
     * Creates ArrayList of mel filters. The filters are distributed evenly over the entire spectrum width.
     * Filters have half width overlay. Its mean, for filters which 100 mel width, first filter has range
     * between 0 and 100 mel, second has range 50 - 150, third has range 100 - 200 etc.
     * @param cl filter class
     * @param spectrumWidth width of spectrum
     * @param filtersNum number of filters
     * @param <T> type of returned filters. It should be the same type as cl parameter.
     * @return ArrayList width filters objects
     */
    private <T extends MelFilter >ArrayList<T> createFilters(Class<T> cl, double spectrumWidth, int filtersNum){
        MelFilterFactor filterFactor = new MelFilterFactor();
        double bandWidth = MelFilter.toMel(spectrumWidth);
        double filterMid = bandWidth/(filtersNum+1);
        return filterFactor.setOfFiltersMel(cl,filterMid*2,filterMid,filtersNum);
    }


    /**
     * Filters spectrum using a list of mel filters. This method return array of double number. Each number is energy
     * for filter band. Size of array is equals to number of filters
     * @param spectrum signal spectrum
     * @param filters ArrayList of filters. Objects in ArrayList must extends MelFilter class.
     * @return array of energy for filters band
     */
    private double[] melScaleFiltering(Spectrum spectrum, ArrayList<? extends MelFilter> filters){

        double[] filtersResults = new double[filters.size()];

        for (int i = 0; i < filters.size(); i++) {
            filtersResults[i] = filters.get(i).filter(spectrum);
        }

        return filtersResults;
    }

    /**
     * Return array of MFCC. When this method is called before invoke calculation, it returns null.
     * @return array of MFCC
     */
    public double[] getMFCC(){
        return coefficients;
    }
}