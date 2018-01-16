package sayPotato.sound;

import java.io.*;
import javax.swing.*;
import javax.sound.sampled.*;



public class SoundSaver {

    private AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
    private File wavFile;

    public void saveRecord(byte [] record, AudioFormat format, String path){

        wavFile = new File(path);
        try{
            AudioInputStream ais = new AudioInputStream(new ByteArrayInputStream(record), format, record.length/format.getFrameSize());
            AudioSystem.write(ais, fileType, wavFile);
            ais.close();
            JOptionPane.showMessageDialog(null, "File save has been saved");
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
