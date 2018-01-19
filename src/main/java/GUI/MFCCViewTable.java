package GUI;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import sayPotato.MFCC;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;

public class MFCCViewTable extends JFrame {


    private JTable mfccTable;
    private JPanel mainPanel;

    private ArrayList<MFCC> mfccArray;

    public MFCCViewTable() throws HeadlessException {
        super();
        setTitle("MFCC");
        int width = 800;
        int height = 400;
        setBounds(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - width / 2,
                Toolkit.getDefaultToolkit().getScreenSize().height / 2 - height / 2,
                width,
                height);
        // GUI initializer generated by IntelliJ IDEA GUI Designer
        // >>> IMPORTANT!! <<<
        // DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
        getContentPane().add(mainPanel);
    }

    public void showMFCCTable(ArrayList<MFCC> mfccs) {
        this.mfccArray = mfccs;
        AbstractTableModel tableModel = new AbstractTableModel() {
            @Override
            public int getRowCount() {
                return mfccArray.get(0).getMFCC().length;
            }

            @Override
            public int getColumnCount() {
                return mfccArray.size();
            }

            @Override
            public Object getValueAt(int i, int i1) {
                return mfccArray.get(i1).getMFCC()[i];
            }

            @Override
            public String getColumnName(int c) {
                return Integer.toString(c);
            }
        };

        mfccTable.setModel(tableModel);
        setVisible(true);
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
        final JScrollPane scrollPane1 = new JScrollPane();
        mainPanel.add(scrollPane1, BorderLayout.CENTER);
        mfccTable = new JTable();
        mfccTable.setAutoResizeMode(0);
        mfccTable.setPreferredScrollableViewportSize(new Dimension(450, 600));
        scrollPane1.setViewportView(mfccTable);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}