package sayPotato;


import be.ac.ulg.montefiore.run.jahmm.*;
import be.ac.ulg.montefiore.run.jahmm.io.*;
import be.ac.ulg.montefiore.run.jahmm.learn.BaumWelchLearner;
import be.ac.ulg.montefiore.run.jahmm.learn.BaumWelchScaledLearner;
import be.ac.ulg.montefiore.run.jahmm.learn.KMeansLearner;
import com.wyskocki.karol.dsp.Spectrum;
import com.wyskocki.karol.dsp.filters.Preemphasis;
import sayPotato.sound.SoundOpener;

import javax.sound.sampled.AudioFormat;
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class HMMWraper {


    int states = 30;    //liczba stanów modelu


    Hmm<ObservationVector> model;

    public HMMWraper(){

    }


    public HMMWraper(String filePath){
        try {
            FileReader fileReader = new FileReader(filePath);
            HmmReader reader = new HmmReader();
            model = reader.read(fileReader, new OpdfMultiGaussianReader());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (FileFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //TODO Odczytywanie modelu z pliku
    }

    public void saveModel(String path){
        try {
            FileWriter writer = new FileWriter(path);
            HmmWriter hmmWriter = new HmmWriter();
            OpdfMultiGaussianWriter opdfWriter = new OpdfMultiGaussianWriter();
            hmmWriter.write(writer, opdfWriter, model);
            DecimalFormat f = new DecimalFormat();


        } catch (IOException e) {
            e.printStackTrace();
        }
        //TODO save model to file.
    }

    public void learn(ArrayList<String> filePaths){
        List<List<ObservationVector>> sequences = new ArrayList<>(filePaths.size());

        //Files reading and MFCCs calculating

        System.out.println("Start reading files");
        for (String path:filePaths) {
            System.out.println("Getting MFCCs from "+path);
            byte[] soundData = SoundOpener.getSoundByteArray(path);
            sequences.add(convertMFCCArrayToListOfObservetionVector(getMFCCs(soundData)));
        }
        System.out.println("End reading files");
        System.out.println("Start learning...");

        System.out.println("Start KMenasLearner...");

        int sumOfObservation = 0;
        for (List<ObservationVector> vectors:sequences) {
            sumOfObservation += vectors.size();
        }

        //states = (int)(sumOfObservation/sequences.size());

        OpdfMultiGaussianFactory factory = new OpdfMultiGaussianFactory(13);
        KMeansLearner< ObservationVector > kml = new KMeansLearner <>(states ,factory, sequences );
        Hmm<ObservationVector> initHmm = kml.iterate();

        System.out.println("End KMenasLearner...");
        System.out.println("Start BaumWelchLearner...");

        BaumWelchScaledLearner bwls = new BaumWelchScaledLearner();
        BaumWelchLearner bwl = new BaumWelchLearner();
        model = bwls.learn (initHmm , sequences);
        System.out.println("End BaumWelchLearner...");


    }

    private ArrayList<MFCC> getMFCCs(byte[] audioSignal){

        //audio
        AudioFormat format = new AudioFormat(44100f, 16, 1, true, false);
        ArrayList<MFCC> mfccArray;

        int overlay = 512;
        int frameSize = 1024;
        Preemphasis preemphasis = new Preemphasis();

        ArrayList<double[]> frames = SignalProcessing.framing(preemphasis.filter(convertToWave(audioSignal)), frameSize, overlay);
        ArrayList<Spectrum> spectrums = SignalProcessing.createSpectrogram(frames, format.getSampleRate());

        mfccArray = new ArrayList<>(spectrums.size());
        for (Spectrum spectrum : spectrums) {
            MFCC mfcc = new MFCC(13, 23);
            mfcc.calculate(spectrum);
            mfccArray.add(mfcc);
        }
        return mfccArray;
    }

    private List<ObservationVector> convertMFCCArrayToListOfObservetionVector(ArrayList<MFCC> mfccs){
        List<ObservationVector> list = new ArrayList<>(mfccs.size());

        for (MFCC mfcc:mfccs) {
            list.add(new ObservationVector(mfcc.getMFCC()));
        }
        return list;
    }

    private double[] convertToWave(byte[] data) {
        double[] signalWave = new double[data.length / 2];

        for (int i = 0; i < signalWave.length; i++) {
            int LSB = (int) data[2 * i];
                         /* Second byte is MSB (high order) */
            int MSB = (int) data[2 * i + 1];
            signalWave[i] = MSB << 8 | (0xFF & LSB);
        }
        return signalWave;
    }

    public double check(List<ObservationVector> sequence){
        double prop = model.probability(sequence);
        System.out.println("Propabylity: "+prop*100 + " %");
        return prop;
        //TODO sprawdzanie, czy obserwacja należy do tego modelu. Powinno zwracać prawdopodobieństwo.
    }


    public static void main(String[] args) {

        ArrayList<HMMWraper> models = new ArrayList<>();
        ArrayList<ArrayList<String>> files = new ArrayList<>();

//        HMMWraper windowModel = new HMMWraper();
//        HMMWraper onModel = new HMMWraper();
//        HMMWraper offModel = new HMMWraper();

        HMMWraper windowModel = new HMMWraper("./model0.hmm");
        HMMWraper onModel = new HMMWraper("./model0.hmm");
        HMMWraper offModel = new HMMWraper("./model0.hmm");

        ArrayList<String> windowFiles = new ArrayList<>(99);
        ArrayList<String> onFiles = new ArrayList<>(99);
        ArrayList<String> offFiles = new ArrayList<>(99);

        for (int i = 1; i <= 99; i++) {
            windowFiles.add("/home/karol/Muzyka/SayPotato/WINDOW_cut/window" + String.format("%03d", i) + ".wav");
            onFiles.add("/home/karol/Muzyka/SayPotato/ON_cut/on" + String.format("%03d", i) + ".wav");
            offFiles.add("/home/karol/Muzyka/SayPotato/OFF_cut/off" + String.format("%03d", i) + ".wav");
        }
        System.out.println("Prepare fake paths");

        models.add(windowModel);
        models.add(onModel);
        models.add(offModel);


        files.add(windowFiles);
        files.add(onFiles);
        files.add(offFiles);

//        for (int i = 0; i < models.size(); i++) {
//            models.get(i).learn(files.get(i));
//        }



        System.out.println("End learning model");

        System.out.println("Testing...");
        ArrayList<String>testPaths = new ArrayList<>();

        testPaths.add("/home/karol/Muzyka/SayPotato/WINDOW_cut/window" + String.format("%03d", 25) + ".wav");
        testPaths.add("/home/karol/Muzyka/SayPotato/WINDOW_cut/window" + String.format("%03d", 100) + ".wav");
        testPaths.add("/home/karol/Muzyka/SayPotato/WINDOW_cut/window" + String.format("%03d", 10) + ".wav");
        testPaths.add("/home/karol/Muzyka/SayPotato/WINDOW_cut/window" + String.format("%03d", 85) + ".wav");
        testPaths.add("/home/karol/Muzyka/SayPotato/pingwinek.wav");
        testPaths.add("/home/karol/Muzyka/SayPotato/cut_kurwa.wav");
        testPaths.add("/home/karol/Muzyka/SayPotato/cut_lamp.wav");
        testPaths.add("/home/karol/Muzyka/SayPotato/cut_light.wav");
        testPaths.add("/home/karol/Muzyka/SayPotato/WINDOW_cut/window" + String.format("%03d", 73) + ".wav");
        testPaths.add("/home/karol/Muzyka/SayPotato/WINDOW_cut/window" + String.format("%03d", 67) + ".wav");
        testPaths.add("/home/karol/Muzyka/SayPotato/WINDOW_cut/window" + String.format("%03d", 83) + ".wav");
        testPaths.add("/home/karol/Muzyka/SayPotato/window_karol.wav");
        testPaths.add("/home/karol/Muzyka/SayPotato/ON_cut/on" + String.format("%03d", 2) + ".wav");
        testPaths.add("/home/karol/Muzyka/SayPotato/ON_cut/on" + String.format("%03d", 60) + ".wav");
        testPaths.add("/home/karol/Muzyka/SayPotato/ON_cut/on" + String.format("%03d", 15) + ".wav");
        testPaths.add("/home/karol/Muzyka/SayPotato/ON_cut/on" + String.format("%03d", 100) + ".wav");
        testPaths.add("/home/karol/Muzyka/SayPotato/ON_cut/on" + String.format("%03d", 75) + ".wav");
        testPaths.add("/home/karol/Muzyka/SayPotato/OFF_cut/off" + String.format("%03d", 2) + ".wav");
        testPaths.add("/home/karol/Muzyka/SayPotato/OFF_cut/off" + String.format("%03d", 60) + ".wav");
        testPaths.add("/home/karol/Muzyka/SayPotato/OFF_cut/off" + String.format("%03d", 15) + ".wav");
        testPaths.add("/home/karol/Muzyka/SayPotato/OFF_cut/off" + String.format("%03d", 100) + ".wav");
        testPaths.add("/home/karol/Muzyka/SayPotato/OFF_cut/off" + String.format("%03d", 75) + ".wav");


//        for (String test:testPaths) {
//            System.out.println("Testing file: "+test);
//            List<ObservationVector> testSequence = getVectorForTest(test);
//            for (HMMWraper model:models) {
//                model.check(testSequence);
//            }
//        }

        double[][] result = new double[models.size()][testPaths.size()];

        for (int i = 0; i < testPaths.size(); i++) {
            System.out.println("Testing file: "+testPaths.get(i));
            List<ObservationVector> testSequence = getVectorForTest(testPaths.get(i));
            for (int j = 0; j < models.size(); j++) {
                result[j][i] = models.get(j).check(testSequence);
            }
        }

        System.out.println(" ");
        System.out.println(" ");


        for (int i = 0; i < testPaths.size(); i++) {
            System.out.print(testPaths.get(i)+"    ");
            for (int j = 0; j < models.size(); j++) {
                System.out.print("| "+result[j][i]+" | ");
            }
            System.out.println(" ");
        }

        System.out.println(" ");

        System.out.println("Save models");
        for (int i = 0; i < models.size(); i++) {
            models.get(i).saveModel("./model"+i+".hmm");
        }

        System.out.println("end");
    }

    private static List<ObservationVector> getVectorForTest(String path){
        List<ObservationVector> sequence = new ArrayList<>();
        byte[] soundData = SoundOpener.getSoundByteArray(path);

        AudioFormat format = new AudioFormat(44100f, 16, 1, true, false);

        int overlay = 512;
        int frameSize = 1024;
        Preemphasis preemphasis = new Preemphasis();

        double[] signalWave = new double[soundData.length / 2];

        for (int i = 0; i < signalWave.length; i++) {
            int LSB = (int) soundData[2 * i];
                         /* Second byte is MSB (high order) */
            int MSB = (int) soundData[2 * i + 1];
            signalWave[i] = MSB << 8 | (0xFF & LSB);
        }

        ArrayList<double[]> frames = SignalProcessing.framing(preemphasis.filter(signalWave), frameSize, overlay);
        ArrayList<Spectrum> spectrums = SignalProcessing.createSpectrogram(frames, format.getSampleRate());

        for (Spectrum spectrum : spectrums) {
            MFCC mfcc = new MFCC(13, 23);
            mfcc.calculate(spectrum);
            sequence.add(new ObservationVector(mfcc.getMFCC()));
        }

        return sequence;
    }

}
