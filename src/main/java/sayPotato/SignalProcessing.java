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


        double[] testowe = new double[128];
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

        spectrum.calculate(testowe, fSapling);

        for (int i = 0; i < spectrum.getFrequency().length; i++) {
            System.out.println(spectrum.getFrequency(i) + " " + spectrum.getSpectrumData(i));
        }

        spectrum.trim(4.0, 70.0);

        System.out.println(" ");
        for (int i = 0; i < spectrum.getFrequency().length; i++) {
            System.out.println(spectrum.getFrequency(i) + " " + spectrum.getSpectrumData(i));
        }
    }
}
