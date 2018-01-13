package sayPotato;

import com.wyskocki.karol.dsp.Spectrum;

/**
 * Abstract class of filter in mel-scale
 * For more information see <a href="en.wikipedia.org/wiki/Mel_scale">Mel scale</a>
 */
public abstract class MelFilter {

    //band limits in Hz
    private double startFreq;
    private double endFreq;

    /**
     * Set band of filter
     * @param startFreq lower cutoff frequency in Hz
     * @param endFreq higher cutoff frequencies in Hz
     */
    public void setBandHz(double startFreq, double endFreq){
        this.startFreq = startFreq;
        this.endFreq = endFreq;
    }

    /**
     * Set band of filter
     * @param startFreq lower cutoff frequency in mel-scale
     * @param endFreq higher cutoff frequencies in mel-scale
     */
    public void setBandMel(double startFreq, double endFreq){
        this.startFreq = toHz(startFreq);
        this.endFreq = toHz(endFreq);
    }

    /**
     * Return width of filter in Hz
     * @return filter width in Hz
     */
    public double getBandWidthHz(){
        return endFreq - startFreq;
    }

    /**
     * Return width of filter in mel-scale
     * @return filter width in mel-scale
     */
    public double getBandWidthMel(){
        return toMel(endFreq) - toMel(startFreq);
    }

    /**
     * return lower cutoff frequency in Hz
     * @return lower cutoff frequency in Hz
     */
    public double getStartFreqHz() {
        return startFreq;
    }

    /**
     * return higher cutoff frequency in Hz
     * @return higher cutoff frequency in Hz
     */
    public double getEndFreqHz() {
        return endFreq;
    }

    /**
     * return lower cutoff frequency in mel-scale
     * @return lower cutoff frequency in mel-scale
     */
    public double getStartFreqMel() {
        return toMel(startFreq);
    }

    /**
     * return higher cutoff frequency in mel-scale
     * @return higher cutoff frequency in mel-scale
     */
    public double getEndFreqMel() {
        return toMel(endFreq);
    }

    /**
     * Translates frequency in Hz scale to mel-scale.
     * f[mel](f[Hz]) = 2595 log10 (1 + f[Hz] /700)
     * @param hz frequency in Hz scale
     * @return frequency in mel-scale
     */
    public static double toMel(double hz){
        return  2595 * Math.log10(1 + (hz/700));
    }

    //

    /**
     * Translates frequency in mel-scale scale to Hz scale.
     * f[Hz](f[mel]) = 700 (10^(f[mel]/2595 -1))
     * @param mel frequency in mel-scale
     * @return frequency in Hz scale
     */
    public static double toHz(double mel){
        return 700 * (Math.pow(10, mel/2595) - 1);
    }

    /**
     * Filters spectrum and returns energy of band.
     * @param spectrum signal spectrum
     * @return energy of band
     */
    public abstract double filter(Spectrum spectrum);

}
