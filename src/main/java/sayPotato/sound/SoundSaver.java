package sayPotato.sound;

import java.io.*;
import javax.swing.*;
import javax.sound.sampled.*;



public class SoundSaver {

    public static void saveRecord(byte [] record, AudioFormat format, String path){
        AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
        File wavFile = new File(path);
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
