package GUI;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;

public class SoundPlayer implements Runnable {

    private Thread thread;
    private SourceDataLine line;
    private PlayerPositionListener listener = null;
    private AudioInputStream inputStream;

    private AudioFormat format;

    private int bufferSize = 16384;
    private double position = -1;
    private boolean running = false;


    public void play(byte[] input, AudioFormat format){
        if (running)
            return;

        running = true;

        this.format = format;

        inputStream = new AudioInputStream(
                new ByteArrayInputStream(input),
                format,
                input.length / format.getFrameSize());

        thread = new Thread(this);
        thread.setName("Playback");
        System.out.println("Start signal");
        thread.start();
    }

    public void stop(){
        System.out.println("Stop play signal");
        thread = null;
    }

    public boolean isRuning(){
        return running;
    }

    @Override
    public void run() {

        DataLine.Info info = new DataLine.Info(SourceDataLine.class,
                format);
        if (!AudioSystem.isLineSupported(info)) {
            System.out.println("Line matching " + info + " not supported.");
            return;
        }

        // open line
        try {
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format, bufferSize);
        } catch (LineUnavailableException ex) {
            System.err.println("Unable to open the line: " + ex);
            return;
        }


        int bufferLengthInFrames = line.getBufferSize() / 8;
        int bufferLengthInBytes = bufferLengthInFrames * format.getFrameSize();
        byte[] data = new byte[bufferLengthInBytes];
        int numBytesRead = 0;

        Thread positionThread = new Thread(new Runnable() {
            @Override
            public void run() {
                updatePosition();
            }
        });

        positionThread.setName("Position");
        positionThread.start();
        running = true;
        System.out.println("Start play");
        line.start();

        while (thread != null) {
            try {
                if ((numBytesRead = inputStream.read(data)) == -1) {
                    break;
                }
                int numBytesRemaining = numBytesRead;
                while (numBytesRemaining > 0) {
                    numBytesRemaining -= line.write(data, 0, numBytesRemaining);
                }
            } catch (Exception e) {
                System.err.println("Error during playback: " + e);
                break;
            }
        }

        if (thread != null) {
            line.drain();
        }
        line.stop();
        line.close();
        line = null;
        thread = null;
        running = false;
        System.out.println("End of track");
    }


    private void updatePosition() {
        while (thread != null) {
            if ((line != null) && (line.isOpen())) {

                double milliseconds = (line.getMicrosecondPosition() / 1000);
                double newPosition = milliseconds / 1000.0;

                if (newPosition > position){
                    position = newPosition;
                    listener.changePosition(position);
                }

                try {
                    thread.sleep(20);
                } catch (Exception e) {
                    break;
                }
            }
        }
        position = -1;
        listener.changePosition(position);
    }


    public void addPositionListener(PlayerPositionListener listener){
        this.listener = listener;
    }


    public interface PlayerPositionListener{
        void changePosition(double position);
    }
}
