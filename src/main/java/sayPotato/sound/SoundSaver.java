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
    
    public void saveRecord(byte [] record, AudioFormat format){

        chooser.addChoosableFileFilter(new FileNameExtensionFilter(".wav", "wav"));
        int returnVal = chooser.showSaveDialog(null);

        if(returnVal == JFileChooser.APPROVE_OPTION){
            path = chooser.getSelectedFile().getPath();
            wavFile = new File(path);
            try{
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
