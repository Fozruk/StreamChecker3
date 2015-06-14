package com.github.fozruk.streamcheckerguitest;

import com.github.epilepticz.streamchecker.model.channel.interf.IChannel;

import java.io.File;
import java.util.List;

/**
 * Created by Philipp on 14.06.2015.
 */
public class PersistenceManager {

    private static  File livestreamer_path;
    private static  File videoplayer_path;
    private static List<IChannel> channelList;

    //Windows Vars
    public static final File APPDATA_FOLDER = new File(System.getenv("APPDATA"));
    public static final File SETTINGS_FOLDER = new File(APPDATA_FOLDER.getAbsolutePath() + "/Streamchecker/");
    public static final File STREAMS_FILE = new File(SETTINGS_FOLDER.getAbsolutePath() + "Streams/");
    public static final File SETTINGS_FILE = new File(SETTINGS_FOLDER.getAbsolutePath() + "Settings/");

    public PersistenceManager()
    {
        loadSettings();
    }

    private void loadSettings()
    {
        String os = System.getenv("os.name");

        if(os.contains("win"))
        {
            loadSettingsWindows();
        } else if(os.indexOf("mac") >= 0)
        {
            loadSettingsMac();
        } else if(os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") >= 0)
        {
            loadSettingsLinux();
        } else
        {
            throw new UnsupportedOperationException();
        }
    }

    private void loadSettingsWindows() {

    }

    private void loadSettingsMac() {

    }

    private void loadSettingsLinux() {

    }
}
