package sayPotato;

import com.wyskocki.karol.dsp.*;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Contains static methods using to preprocessing audio signal, before speech recognition.
 */
public class SignalProcessing {

    /**
     * Splits the audio signal into frames.
     * @param data input audio signal
     * @param frameSize number of samples in frame
     * @param overlay number of overlayed samples
     * @return Frames array. Single frame is array of double.
     */
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

    /**
     * Creates array list of spectrums of framed signal.
     * @param frames array list of signal frames
     * @param fSampling sampling frequency of signal
     * @return array list of spectrums
     */
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
}
