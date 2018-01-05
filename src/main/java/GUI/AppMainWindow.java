package GUI;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.wyskocki.karol.dsp.Spectrum;
import org.jfree.chart.ChartPanel;
import sayPotato.SignalProcessing;
import sayPotato.SoundPlayer;
import sayPotato.SoundRecorder;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class AppMainWindow extends JFrame {


    private JPanel mainPanel;
    private JPanel signalPanel;
    private JButton recordBtn;
    private JButton playBtn;
    private JButton analysisBtn;
    private JPanel spectrumPanel;

    private SignalView signalView;
    private SpectrumView spectrumView;

    //audio
    AudioFormat format = new AudioFormat(44100f, 16, 1, true, false);

    SoundRecorder recorder;
    SoundPlayer player;

    private byte[] audioSignal;

    public AppMainWindow(String windowName) {
        super(windowName);

        // GUI initializer generated by IntelliJ IDEA GUI Designer
        // >>> IMPORTANT!! <<<
        // DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
        getContentPane().add(mainPanel);

        recorder = new SoundRecorder();
        player = new SoundPlayer();

        player.addPositionListener(new SoundPlayer.PlayerPositionListener() {
            @Override
            public void changePosition(final double position) {
                //System.out.println("Update position");
                try {
                    EventQueue.invokeAndWait(new Runnable() {
                        @Override
                        public void run() {
                            if (position >= 0) {
                                signalView.setPointer(position);
                            } else {
                                if (!player.isRuning())
                                    stopPlay();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        recordBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if (!recorder.isRunning()) {
                    startRecording();
                } else {
                    stopRecording();
                }
            }
        });


        playBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (playBtn.getText().equals("Play"))
                    startPlay(audioSignal, format);
                else
                    stopPlay();
            }
        });
        analysisBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (audioSignal != null) {
                    int overlay = 64;
                    int frameSize = 1024;
                    double spectrumSpacing = (frameSize / format.getSampleRate()) - (overlay / format.getSampleRate());

                    ArrayList<double[]> frames = SignalProcessing.framing(convertToWave(audioSignal), frameSize, overlay);
                    ArrayList<Spectrum> spectrums = SignalProcessing.createSpectrogram(frames, format.getSampleRate());
                    spectrumView.setSpectrum(spectrums, spectrumSpacing);

                    System.out.println("Audio length: " + audioSignal.length / 2 * (1 / format.getSampleRate()) + " s");

                }
            }
        });
    }


    private void startRecording() {

        stopPlay();
        playBtn.setEnabled(false);
        recordBtn.setText("Stop");

        signalView.clearPlots();

        Thread stopper = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(5010);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                if (recorder.isRunning())
                    stopRecording();
            }
        });

        recorder.startRecord(format);

        while (!recorder.isRunning()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        stopper.start();
    }

    private void stopRecording() {
        System.out.println("Stop signal");
        recorder.stop();

        //wait until recorder is running
        while (recorder.isRunning()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        audioSignal = recorder.getSoundRecord();
        signalView.setData(convertToWave(audioSignal), format.getSampleRate());

        playBtn.setEnabled(true);
        recordBtn.setText("Record");
    }

    private void startPlay(byte[] audioSignal, AudioFormat format) {
        if (audioSignal != null) {
            player.play(audioSignal, format);
            playBtn.setText("Stop");
        }
    }

    private void stopPlay() {
        if (player.isRuning()) {
            player.stop();
        }
        playBtn.setText("Play");
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

    private void createUIComponents() {
        signalPanel = new JPanel();
        signalPanel.setLayout(new BorderLayout());

        signalView = new SignalView();
        ChartPanel chartPanel = new ChartPanel(signalView.getChart());
        chartPanel.setRefreshBuffer(true);
        chartPanel.setMouseWheelEnabled(true);
        chartPanel.setRangeZoomable(false);
        signalPanel.add(chartPanel, BorderLayout.CENTER);

        spectrumPanel = new JPanel();
        spectrumPanel.setLayout(new BorderLayout());

        spectrumView = new SpectrumView();
        ChartPanel spectrumChartPanel = new ChartPanel(spectrumView.getChart());
        spectrumPanel.add(spectrumChartPanel, BorderLayout.CENTER);

    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Voice record"));
        recordBtn = new JButton();
        recordBtn.setText("Record");
        panel1.add(recordBtn, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        playBtn = new JButton();
        playBtn.setText("Play");
        panel1.add(playBtn, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel1.add(signalPanel, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, new Dimension(-1, 200), new Dimension(-1, 200), null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Spectrum"));
        panel2.add(spectrumPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        analysisBtn = new JButton();
        analysisBtn.setText("Analysis");
        panel2.add(analysisBtn, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
