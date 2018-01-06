package sayPotato;

import com.wyskocki.karol.dsp.Spectrum;

import java.util.ArrayList;

public class MelFilter {

    //band limits in Hz
    double startFreq;
    double endFreq;

    public void setBandHz(double startFreq, double endFreq){
        this.startFreq = startFreq;
        this.endFreq = endFreq;
    }

    public void setBandMel(double startFreq, double endFreq){
        this.startFreq = toHz(startFreq);
        this.endFreq = toHz(endFreq);
    }

    public double getBandWidthHz(){
        return endFreq - startFreq;
    }

    public double getBandWidthMel(){
        return toMel(endFreq) - toMel(startFreq);
    }

    public double getStartFreqHz() {
        return startFreq;
    }

    public double getEndFreqHz() {
        return endFreq;
    }

    public double getStartFreqMel() {
        return toMel(startFreq);
    }

    public double getEndFreqMel() {
        return toMel(endFreq);
    }

    //f mel (f Hz ) = 2595 log 10 (1 + f Hz /700)

    public static double toMel(double hz){
        return  2595 * Math.log10(1 + (hz/700));
    }

    //f Hz (f mel ) = 700 (10 fmel/2595 -1)

    public static double toHz(double mel){
        return 700 * (Math.pow(10, mel/2595) - 1);
    }

    public double filter(Spectrum spectrum){
        double midOfFilter = toHz(toMel(startFreq) + (getBandWidthMel())/2);
        double midofFilterMel = toMel(midOfFilter);

        double coefA1 = 1 / (midofFilterMel - getStartFreqMel());
        double coefB1 = -coefA1*getStartFreqMel();

        double coefA2 = -1 / (getEndFreqMel() - midofFilterMel);
        double coefB2 = 1 - coefA2*midofFilterMel;

        double bandEnergy = 0;

        double sample;
        double filterCoef;

        for (int i = 0; i < spectrum.getSpectrumData().length; i++) {

            if(spectrum.getFrequency(i) >= startFreq && spectrum.getFrequency(i) <= midOfFilter){
                filterCoef = coefA1 * toMel(spectrum.getFrequency(i)) + coefB1;
                sample = Math.pow(spectrum.getSpectrumData(i), 2) * filterCoef;

            }else if (spectrum.getFrequency(i) > midOfFilter && spectrum.getFrequency(i) <= endFreq){
                filterCoef = coefA2 * toMel(spectrum.getFrequency(i)) + coefB2;
                sample = Math.pow(spectrum.getSpectrumData(i), 2) * filterCoef;

            }else {
                sample = 0;
            }

            bandEnergy += sample;
        }

        return bandEnergy;
    }



    public static void main(String[] args) {
        MelFilter filter = new MelFilter();

        filter.setBandMel(600, 1200);

        double[] signal = new double[1024];

        for (int i = 0; i < signal.length; i++) {
            signal[i] = Math.random()*100;
        }

        ArrayList<double[]> dataForSpectum = new ArrayList<>();
        dataForSpectum.add(signal);

        ArrayList<Spectrum> spectrumArray = SignalProcessing.createSpectrogram(dataForSpectum, 44100);

        double bandEnergy = filter.filter(spectrumArray.get(0));

        System.out.println(bandEnergy);


    }
}
