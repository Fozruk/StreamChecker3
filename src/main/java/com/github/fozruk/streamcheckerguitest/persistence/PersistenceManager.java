package com.github.fozruk.streamcheckerguitest.persistence;

import java.io.File;
import java.io.IOException;

/**
 * Created by Philipp on 14.06.2015.
 */
public abstract class PersistenceManager {

    public static enum OperatingSystem {Windows,Linux,Mac};
    private OperatingSystem os;

    //Windows Vars
    public static final File APPDATA_FOLDER = new File(System.getenv("APPDATA"));
    public static final File SETTINGS_FOLDER = new File(APPDATA_FOLDER.getAbsolutePath() + "/Streamchecker/");
    public static final File STREAMS_FILE = new File(SETTINGS_FOLDER.getAbsolutePath() + "/Streams/");
    public static final File SETTINGS_FILE = new File(SETTINGS_FOLDER.getAbsolutePath() + "/Settings/");

    protected PersistenceManager() throws IOException {
        os = OperatingSystem.Windows;
        if(!SETTINGS_FOLDER.exists())
        {
            SETTINGS_FOLDER.mkdir();
        }

        if(!STREAMS_FILE.exists())
        {
            STREAMS_FILE.createNewFile();
        }

        if(!SETTINGS_FILE.exists())
        {
            SETTINGS_FILE.createNewFile();
        }
    }

    public OperatingSystem getOs()
    {
        return os;
    }

    public void setOs(OperatingSystem os)
    {
        this.os = os;
    }

}
