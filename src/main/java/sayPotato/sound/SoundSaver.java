package sayPotato.sound;




import java.io.*;
import javax.swing.*;
import javax.sound.sampled.*;
import javax.swing.filechooser.FileNameExtensionFilter;


public class SoundSaver {

    private JFileChooser chooser = new JFileChooser();
    private AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
    private String path;
    private File wavFile;

    AudioFormat getAudioFormat(){
        float sampleRate = 44100;
        int sampleSizeInBits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = true;
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed,bigEndian);
        return format;
    }

    public void saveRecord(byte [] record){

        chooser.addChoosableFileFilter(new FileNameExtensionFilter(".wav", "wav"));
        int returnVal = chooser.showSaveDialog(null);

        if(returnVal == JFileChooser.APPROVE_OPTION){
            path = chooser.getSelectedFile().getPath();
            wavFile = new File(path);
            try{
                AudioFormat format = getAudioFormat();
                AudioInputStream ais = new AudioInputStream(new ByteArrayInputStream(record), format, record.length);

                AudioSystem.write(ais, fileType, wavFile);
                ais.close();
                JOptionPane.showMessageDialog(null, "File save has been saved");
            }catch(Exception e){
                e.printStackTrace();
            }
        }else if(returnVal == JFileChooser.CANCEL_OPTION){
            JOptionPane.showMessageDialog(null, "File save has been canceled");
        }
    }

}
