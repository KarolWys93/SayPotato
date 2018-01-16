package sayPotato.sound;

import java.io.*;
import javax.sound.sampled.*;


public class SoundOpener {


    public static byte[] getSoundByteArray(String path) {
        byte[] audio = null;
        File wavFile = new File(path);

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
