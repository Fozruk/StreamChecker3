package com.github.fozruk.streamcheckerguitest.vlcgui.ui;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Created by Philipp on 20.10.2015.
 */
public class FileChooser extends JFileChooser {
    public FileChooser(String
            filename)
    {
        setDialogTitle("Find Application");
        setFileSelectionMode(FILES_ONLY);
        setFileFilter(new FileFilter() {
            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(filename)
                        || f.isDirectory();
            }

            public String getDescription() {
                return filename +"(*.exe)";
            }
        });


    }

    public File getLocationWithFileChooser()
    {
        File file = null;
        int retval = showOpenDialog(null);
        if (retval == JFileChooser.APPROVE_OPTION) {
            file = getSelectedFile();
        }
        return file;
    }
}
