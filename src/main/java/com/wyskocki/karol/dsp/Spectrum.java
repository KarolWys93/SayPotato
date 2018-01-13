package com.wyskocki.karol.dsp;

import java.util.Arrays;

/**
 * Calculate frequency spectrum of signal.
 */
public class Spectrum {

    private double[] spectrumData = null;
    private double[] freqList = null;
    private double freqDelta = 0;

    /**
     * Calculate frequency spectrum of signal
     * @param data signal
     * @param samplingFreq sampling frequency of signal
     */
    public void calculate(double[] data, double samplingFreq){

        Complex[] complexData = new Complex[data.length];

        for (int i = 0; i < data.length; i++) {
            complexData[i] = new Complex(data[i], 0.0);
        }

        Complex[] fft = FFT.fft(complexData);
        fft = fftTrim(fft);

        spectrumData = new double[fft.length];

        for (int i = 0; i < fft.length; i++) {
            spectrumData[i] = fft[i].abs()/data.length;
        }

        freqDelta = samplingFreq/data.length;

        freqList = freqRange(freqDelta, spectrumData.length);
    }

    /**
     * Cuts the spectrum to the desired band.
     * @param startF
     * @param endF
     */
    public void trim(double startF, double endF){

        if(startF > endF)
            throw new IllegalArgumentException("start frequency can't be higher than end frequency");

        int startIndex = 0;
        int endIndex = freqList.length+1;
        double toStartDistance = Double.MAX_VALUE;

        for (int i = 0; i < freqList.length; i++){
            if(freqList[i] - startF >= 0 && freqList[i] - startF < toStartDistance){
                toStartDistance = freqList[i] - startF;
                startIndex = i;
            }

            if(freqList[i] < endF){
                endIndex = i;
            }
        }

        if (endF > freqList[freqList.length-1])
            endIndex = freqList.length;

        freqList = Arrays.copyOfRange(freqList, startIndex, endIndex);
        spectrumData = Arrays.copyOfRange(spectrumData, startIndex, endIndex);
    }

    /**
     * Returns array of spectrum data
     * @return spectrum
     */
    public double[] getSpectrumData() {
        return spectrumData;
    }

    /**
     * Returns array of frequencies corresponding to spectrum data array
     * @return array of frequencies
     */
    public double[] getFrequency() {
        return freqList;
    }

    /**
     * Returns spectrum value for i element.
     * @param i number of spectrum part
     * @return value of sepctrum for i element
     */
    public double getSpectrumData(int i) {
        return spectrumData[i];
    }

    /**
     * Returns frequency value of i element of spectrum
     * @param i number of spectrum part
     * @return frequency value
     */
    public double getFrequency(int i) {
        return freqList[i];
    }

    /**
     * Returns the value of spectrum resolution.
     * @return frequency step
     */
    public double getFreqDelta() {
        return freqDelta;
    }

    /**
     * Returns width of spectrum
     * @return spectrum width in Hz
     */
    public double getSpectrumWidth(){
        return freqList[freqList.length-1];
    }

    /**
     * calculate values of frequency array using number of spectrum elements and spectrum resolutions.
     * @param freqDelta spectrum resolution
     * @param spectrumWidth
     * @return
     */
    private double[] freqRange(double freqDelta, int spectrumWidth){
        double[] freqScale = new double[spectrumWidth];

        for (int i = 0; i < freqScale.length; i++) {
            freqScale[i] = i * freqDelta;
        }
        return freqScale;
    }

    private Complex[] fftTrim(Complex[] fft){
        return Arrays.copyOfRange(fft, 0, fft.length % 2 != 0 ? fft.length/2+1 : fft.length/2);
    }

}
