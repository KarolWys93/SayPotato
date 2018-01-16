package sayPotato.sound;

import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Recording sound
 */
public class SoundRecorder implements Runnable {

    private Thread thread = null;
    private TargetDataLine line = null;
    private AudioFormat format =null;
    private byte[] soundRecord = null;

    private boolean running = false;

    /**
     * Starts recording sound
     * @param format record data format
     */
    public void startRecord(AudioFormat format){

        this.format = format;

        thread = new Thread(this);
        thread.setName("Recording");
        thread.start();
    }

    /**
     * Stops recording
     */
    public void stop(){
        thread = null;
    }

    /**
     * Return true, if recording is running
     * @return
     */
    public boolean isRunning(){
        return running;
    }

    @Override
    public void run() {
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format); // format is an AudioFormat object
        if (!AudioSystem.isLineSupported(info)) {
            System.err.println("Line not supported");
        }

        try {
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            System.out.println(line.getLineInfo().toString());
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        System.out.println("Start recording");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int frameSizeInBytes = format.getFrameSize();
        int bufferLengthInFrames = line.getBufferSize() / 8;
        int bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
        byte[] data = new byte[bufferLengthInBytes];
        int numBytesRead;

        running = true;
        line.start();

        while (thread!=null) {
            if ((numBytesRead = line.read(data, 0, bufferLengthInBytes)) == -1) {
                break;
            }
            out.write(data, 0, numBytesRead);
        }

        line.stop();
        line.close();
        line = null;

        // stop and close the output stream
        try {
            out.flush();
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        soundRecord = out.toByteArray();
        running = false;
        System.out.println("End of record");

    }

    /**
     * Returns recorded sound as array of bytes.
     * @return
     */
    public byte[] getSoundRecord(){
        return soundRecord;
    }
}
