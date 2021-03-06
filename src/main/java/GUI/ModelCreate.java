package GUI;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import sayPotato.HMMContainer;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class ModelCreate extends JPanel {
    private JSpinner statesSpinner;
    private JSpinner iterationsSpinner;
    private JPanel mainPanel;
    private JButton loadTrainingDataButton;
    private JButton trainNewModelButton;
    private JButton saveModelButton;
    private JTextField modelNameField;
    private JLabel trainDataNumLabel;
    private JLabel trainingStatusLabel;
    private JFileChooser trainDataChooser;
    private JFileChooser saveModelFileChooser;
    private ArrayList<String> pathList;

    private HMMContainer modelContainer;
    private boolean trainingDone = true;
    SwingWorker trainer;

    public ModelCreate() {
        $$$setupUI$$$();

        add(mainPanel);

        trainDataChooser = new JFileChooser();
        trainDataChooser.addChoosableFileFilter(new FileNameExtensionFilter(".wav", "wav"));
        pathList = new ArrayList<>();

        saveModelFileChooser = new JFileChooser();
        saveModelFileChooser.addChoosableFileFilter(new FileNameExtensionFilter(".mdl", "model"));

        loadTrainingDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                trainDataChooser.setMultiSelectionEnabled(true);
                int result = trainDataChooser.showOpenDialog(null);

                if (result == JFileChooser.APPROVE_OPTION) {
                    pathList.clear();
                    File[] files = trainDataChooser.getSelectedFiles();
                    for (File file : files) {
                        pathList.add(file.getPath());
                    }
                    trainDataNumLabel.setText(Integer.toString(pathList.size()));
                    trainingStatusLabel.setText("untrained");
                }
            }
        });
        trainNewModelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                modelContainer = new HMMContainer(
                        modelNameField.getText(),
                        (Integer) statesSpinner.getValue(),
                        (Integer) iterationsSpinner.getValue());

                trainingDone = false;

                loadTrainingDataButton.setEnabled(false);
                trainNewModelButton.setEnabled(false);
                saveModelButton.setEnabled(false);

                trainer = new SwingWorker() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        modelContainer.learn(pathList);
                        return null;
                    }

                    @Override
                    protected void done() {
                        super.done();
                        trainingDone = true;
                        trainingStatusLabel.setText("trained");
                        trainNewModelButton.setText("Train new model");
                        loadTrainingDataButton.setEnabled(true);
                        trainNewModelButton.setEnabled(true);
                        saveModelButton.setEnabled(true);
                    }
                };

                trainer.execute();
                trainingStatusLabel.setText("training in progress...");
            }
        });
        saveModelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (trainingDone == true && modelContainer != null) {
                    if (saveModelFileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                        File modelFile = saveModelFileChooser.getSelectedFile();
                        modelContainer.saveModel(modelFile.getPath());
                    }
                }
            }
        });
    }

    private void createUIComponents() {
        statesSpinner = new JSpinner(new SpinnerNumberModel(30, 2, 40, 1));
        iterationsSpinner = new JSpinner(new SpinnerNumberModel(9, 1, 20, 1));
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
        mainPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(160, 359), null, 0, false));
        loadTrainingDataButton = new JButton();
        loadTrainingDataButton.setText("Load training data");
        panel1.add(loadTrainingDataButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        trainNewModelButton = new JButton();
        trainNewModelButton.setText("Train new model");
        panel1.add(trainNewModelButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        saveModelButton = new JButton();
        saveModelButton.setText("Save model");
        panel1.add(saveModelButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(5, 2, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(272, 359), null, 0, false));
        panel2.setBorder(BorderFactory.createTitledBorder("Model"));
        final JLabel label1 = new JLabel();
        label1.setText("Model name: ");
        panel2.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTHEAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        modelNameField = new JTextField();
        panel2.add(modelNameField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Iterations: ");
        panel2.add(label2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_NORTHEAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Training data: ");
        panel2.add(label3, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_NORTHEAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Status: ");
        panel2.add(label4, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_NORTHEAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel2.add(statesSpinner, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("States: ");
        panel2.add(label5, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTHEAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel2.add(iterationsSpinner, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        trainDataNumLabel = new JLabel();
        trainDataNumLabel.setText("0");
        panel2.add(trainDataNumLabel, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        trainingStatusLabel = new JLabel();
        trainingStatusLabel.setText("Untrained");
        panel2.add(trainingStatusLabel, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
