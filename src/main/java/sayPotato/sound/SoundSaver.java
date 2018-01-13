package sayPotato.sound;




import java.io.*;
import javax.swing.*;
import javax.sound.sampled.*;



public class SoundSaver {

    private JFileChooser chooser = new JFileChooser();
    private AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
    private String path;

    AudioFormat getAudioFormat(){
        float sampleRate = 44100;
        int sampleSizeInBits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = true;
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed,bigEndian);
        return format;
    }

    public String getSavePath(){

        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = chooser.showSaveDialog(null);

        if(returnVal == JFileChooser.APPROVE_OPTION){
            path = chooser.getSelectedFile().getPath();
        }

        return path;
    }//getSavePath


    public void saveRecord(byte [] record, String path){

        File wavFile = new File(path);

        try{
            AudioFormat format = getAudioFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            AudioInputStream ais = new AudioInputStream(new ByteArrayInputStream(record), format, record.length);

            AudioSystem.write(ais, fileType, wavFile);

        }catch(Exception e){
            e.printStackTrace();
        }






    }//saveToWave


}
