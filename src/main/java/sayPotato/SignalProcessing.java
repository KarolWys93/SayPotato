package sayPotato;

import com.wyskocki.karol.dsp.*;
import com.wyskocki.karol.dsp.filters.DigitalFilter;
import com.wyskocki.karol.dsp.filters.Preemphasis;

import java.util.ArrayList;
import java.util.Arrays;

public class SignalProcessing {


    public static ArrayList<double[]> framing(double[] data, int frameSize, int overlay){
        ArrayList<double[]> framesTmp = new ArrayList<>(2*frameSize);

        int start = 0;
        int end = frameSize;

        while (end <= data.length){
            framesTmp.add(Arrays.copyOfRange(data, start, end));
            start = end - overlay;
            end = start + frameSize;
        }

        if(end > data.length){
            double[] tmp = new double[frameSize];
            for(int i = 0; i + start < data.length; i++){
                if (i + start < data.length){
                    tmp[i] = data[start + i];
                }
            }
            framesTmp.add(tmp);
        }
        return framesTmp;
    }

    public static ArrayList<Spectrum> createSpectrogram(ArrayList<double[]> frames, double fSampling){
        ArrayList<Spectrum> spectrums = new ArrayList<>(frames.size());
        HammingWindow window = new HammingWindow();
        for (double [] frame:frames) {
            Spectrum spectrum = new Spectrum();
            window.processing(frame);
            spectrum.calculate(frame, fSampling);
            spectrum.trim(0, 8000);
            spectrums.add(spectrum);
        }
        return spectrums;
    }

    public static <T extends MelFilter >ArrayList<T> createFilters(Class<T> cl, double spectrumWidth, int filtersNum){
        MelFilterFactor filterFactor = new MelFilterFactor();
        int filterNum = filtersNum;
        double bandWidth = MelFilter.toMel(spectrumWidth);
        double filterMid = bandWidth/(filterNum+1);
        return filterFactor.setOfFiltersMel(cl,filterMid*2,filterMid,filterNum);
    }

    public static double[] melScaleFiltering(Spectrum spectrum, ArrayList<? extends MelFilter> filters){

        double[] filtersResults = new double[filters.size()];
        int i = 0;
        for (MelFilter filter : filters) {
            filtersResults[i] = filter.filter(spectrum);
            i++;
        }

        return filtersResults;
    }

    public static ArrayList<double[]> mfcc(double[] data, int mfccCoeffs){

        ArrayList<double[]> frames = SignalProcessing.framing(data, 1024, 512);

        ArrayList<Spectrum> spectrums = SignalProcessing.createSpectrogram(frames, 44100);

        int filterNum = 22;
        double bandWidth = spectrums.get(0).getSpectrumWidth();
        ArrayList<TriangleMelFilter> filters = SignalProcessing.createFilters(TriangleMelFilter.class, bandWidth, filterNum);

        ArrayList<double[]>melCoef = new ArrayList<>(spectrums.size());
        for (Spectrum spectrum:spectrums) {
            double[] xyz = (melScaleFiltering(spectrum, filters));

            for (int i = 0; i < xyz.length; i++) {
                xyz[i] = Math.log10(xyz[i]);
            }

            xyz = DCT.dct(xyz, mfccCoeffs+1);
            melCoef.add(Arrays.copyOfRange(xyz, 1, xyz.length));
        }

        return melCoef;
    }


    public static void main(String[] args) {

        double[] testData = new double[100];
        for (int i = 0; i < testData.length; i++){
            testData[i] = 1;//Math.random();
        }

        ArrayList<double[]> result = SignalProcessing.framing(testData, 15, 2);

        for (double[] d:result) {
            for (int i = 0; i < d.length; i++){
                System.out.print(d[i]+" ");
            }
            System.out.println("");
        }

        DigitalFilter preemphasis = new Preemphasis();

        //testData = preemphasis.filter(testData);

        HammingWindow window = new HammingWindow();


        double[] testowe = new double[1024*10];
        double fSapling = 100;
        double freq = 5;
        double amp = 1;
        double pulsacja = 2*Math.PI*freq;
        double deltaT = 1/fSapling;

        double freq2 = 20;
        double amp2 = 2;
        double pulsacja2 = 2*Math.PI*freq2;

        for (int i = 0; i<testowe.length; i++){
            //testowe[i] = new Complex(amp * Math.sin(pulsacja*(deltaT*i)), 0.0);
            testowe[i] = amp * Math.sin(pulsacja*(deltaT*i));
            testowe[i] += amp2 * Math.sin(pulsacja2*(deltaT*i));

            System.out.println(i + " " + testowe[i]);
        }

        System.out.println("");

        Spectrum spectrum = new Spectrum();

        window.processing(testowe);

//        spectrum.calculate(testowe, fSapling);

//        for (int i = 0; i < spectrum.getFrequency().length; i++) {
//            System.out.println(spectrum.getFrequency(i) + " " + spectrum.getSpectrumData(i));
//        }

//        spectrum.trim(4.0, 70.0);

//        System.out.println(" ");
//        for (int i = 0; i < spectrum.getFrequency().length; i++) {
//            System.out.println(spectrum.getFrequency(i) + " " + spectrum.getSpectrumData(i));
//        }

        System.out.println(" ");
        System.out.println(" ");

        for (double[] data:SignalProcessing.mfcc(testowe, 13)) {
            for (int i = 0; i < data.length; i++) {
                System.out.print(data[i]+" ");
            }
            System.out.println("");
        }
    }
}
