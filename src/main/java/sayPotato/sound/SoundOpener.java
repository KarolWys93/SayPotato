package sayPotato.sound;

import java.io.*;
import javax.swing.*;
import javax.sound.sampled.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SoundOpener {

    private JFileChooser chooser = new JFileChooser();
    private byte[] audio;
    private String path;
    private File wavFile;


    public String getFilePath() {

        chooser.addChoosableFileFilter(new FileNameExtensionFilter(".wav", "wav"));
        int returnVal = chooser.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            path = chooser.getSelectedFile().getPath();
        } else if (returnVal == JFileChooser.CANCEL_OPTION) {
            JOptionPane.showMessageDialog(null, "File opening has been canceled");
        }
        return path;
    }

    public byte[] getSoundByteArray() {

        wavFile = new File(path);

        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(wavFile);
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

            byte[] buf = new byte[16 * 1024];
            int readed;
            do {
                readed = ais.read(buf, 0, buf.length);
                if (readed >= 0) {
                    byteStream.write(buf, 0, readed);
                }
            } while (readed >= 0);
            audio = byteStream.toByteArray();
        } catch (IOException ioExceptione) {
            ioExceptione.printStackTrace();
        }catch (UnsupportedAudioFileException ue){
            ue.printStackTrace();
        }
        return audio;
    }
}
