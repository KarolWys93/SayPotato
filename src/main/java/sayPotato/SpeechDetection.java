package sayPotato;

import java.util.ArrayList;
import java.util.Arrays;

public class SpeechDetection {

    private double itl = 0;
    private double itu = 0;
    private double izct = 0;

    private ArrayList<SpeechDetection.Section>words = new ArrayList<>();


    public void speechDetect(double[] audioSignal, double samplingFrequency) {
        words.clear();
        double frameLength = 0.01; //10 ms

        ArrayList<double[]> frames= SignalProcessing.framing(audioSignal, (int) (frameLength * samplingFrequency), 0);

        double[] energy = new double[frames.size()];
        double[] zcr = new double[frames.size()];

        for (int i = 0; i < frames.size(); i++) {
            double [] frame = frames.get(i);
            double e = Math.abs(frame[0]);
            double zeroPass = 0;

            for (int j = 1; j < frame.length; j++) {
                e += Math.abs(frame[j]);
                zeroPass += Math.abs((frame[j] >= 0? 1 : -1) - (frame[j-1] >= 0? 1 : -1));
            }

            energy[i] = e;
            zcr[i] = zeroPass/2;
        }
        getThresholdValues(Arrays.copyOfRange(energy,0,(int) (0.1/frameLength)),
                Arrays.copyOfRange(zcr,0,(int) (0.1/frameLength)));

        searchByEnergy(energy);
        searchByZCR(zcr);

    }


    public ArrayList<Section> getWords() {
        return words;
    }

    private void searchByEnergy(double[] energyFrames){
        boolean candidate = false;
        boolean searchFirst = true;
        int n1 = -1;
        int n2 = -1;

        for (int i = 0; i < energyFrames.length; i++) {
            if (searchFirst){
                if(energyFrames[i] > itl){
                    if(!candidate){
                        n1 = i;
                        candidate = true;
                    }
                    if (energyFrames[i] > itu){
                        searchFirst = false;
                        candidate = false;
                    }
                }else {
                    candidate = false;
                }
            }else {
                if (energyFrames[i] < itl){
                    searchFirst = true;
                    n2 = i;
                    words.add(new Section(n1, n2));
                    n1 = -1;
                    n2 = -1;
                }
            }
        }

        if (n1 != -1 && n2 == -1){
            words.add(new Section(n1, energyFrames.length-1));
        }
    }

    private void searchByZCR(double[] zcr){
        int frames250ms = 25;    //if frame length is 10 ms

        int test = 0;
        for (Section section: words) {
            test++;
            int oldStart = section.start;
            int oldEnd = section.end;

            int searchEnd = oldStart >= frames250ms ? oldStart-frames250ms : 0;
            int crossingCounter = 0;

            for (int i = oldStart; i >= searchEnd; i--) {
                if(zcr[i] >= izct){
                    crossingCounter++;
                }else{
                    crossingCounter = 0;
                }
                if (crossingCounter >= 3){
                    section.start = i;
                    break;
                }
            }


            searchEnd = oldEnd+frames250ms < zcr.length ? oldEnd+frames250ms : zcr.length;
            crossingCounter = 0;

            for (int i = oldEnd; i < searchEnd; i++) {
                if(zcr[i] >= izct){
                    crossingCounter++;
                }else{
                    crossingCounter = 0;
                }
                if (crossingCounter >= 3){
                    section.end = i;
                    break;
                }
            }


        }
    }

    private void getThresholdValues(double[] energy, double[] zcr){

        double imn = Utils.meanValue(energy);
        double imx = Utils.getMaxValue(energy);
        itl = Math.min(0.03*(imx-imn)+imn, 4*imn);
        itu = 5*itl;

        izct = Math.min(25, Utils.meanValue(zcr) + 2*Utils.standardDeviation(zcr));

    }



    public class Section {
        public int start;
        public int end;
        public Section(int start, int end){
            this.start = start;
            this.end = end;
        }
        @Override
        public String toString(){
            return start + " : " + end;
        }
    }

}
