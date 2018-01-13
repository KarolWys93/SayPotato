package sayPotato;

import com.wyskocki.karol.dsp.Spectrum;

/**
 * This type of mel-scale filter has shape of triangle window function.
 */
public class TriangleMelFilter extends MelFilter{

    @Override
    public double filter(Spectrum spectrum){
        double midOfFilter = toHz(getStartFreqMel() + (getBandWidthMel())/2);
        double midOfFilterMel = toMel(midOfFilter);

        double coefA1 = 1 / (midOfFilterMel - getStartFreqMel());
        double coefB1 = -coefA1*getStartFreqMel();

        double coefA2 = -1 / (getEndFreqMel() - midOfFilterMel);
        double coefB2 = 1 - coefA2*midOfFilterMel;

        double bandEnergy = 0;

        double sample;
        double filterCoef;

        for (int i = 0; i < spectrum.getSpectrumData().length; i++) {
            if(spectrum.getFrequency(i) >= getStartFreqHz() && spectrum.getFrequency(i) <= midOfFilter){
                filterCoef = coefA1 * toMel(spectrum.getFrequency(i)) + coefB1;
                sample = Math.pow(spectrum.getSpectrumData(i), 2) * filterCoef;
            }else if (spectrum.getFrequency(i) > midOfFilter && spectrum.getFrequency(i) <= getEndFreqHz()){
                filterCoef = coefA2 * toMel(spectrum.getFrequency(i)) + coefB2;
                sample = Math.pow(spectrum.getSpectrumData(i), 2) * filterCoef;
            }else {
                sample = 0;
            }
            bandEnergy += sample;
        }
        return bandEnergy;
    }

}
