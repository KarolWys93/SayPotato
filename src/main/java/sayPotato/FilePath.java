package sayPotato;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FilePath {

    private JFileChooser chooser = new JFileChooser();
    private String path;

    public String getSavePath(){
        chooser.addChoosableFileFilter(new FileNameExtensionFilter(".wav", "wav"));
        int returnVal = chooser.showSaveDialog(null);

        if(returnVal == JFileChooser.APPROVE_OPTION){
            path = chooser.getSelectedFile().getPath();


        }else if(returnVal == JFileChooser.CANCEL_OPTION){
            JOptionPane.showMessageDialog(null, "File save has been canceled");
        }
        return path;
    }

    public String getOpenPath() {

        chooser.addChoosableFileFilter(new FileNameExtensionFilter(".wav", "wav"));
        int returnVal = chooser.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            path = chooser.getSelectedFile().getPath();
        } else if (returnVal == JFileChooser.CANCEL_OPTION) {
            JOptionPane.showMessageDialog(null, "File opening has been canceled");
        }
        return path;
    }
}
