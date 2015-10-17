package com.github.fozruk.streamcheckerguitest.persistence;

import java.io.File;
import java.io.IOException;

/**
 * Created by Philipp on 14.06.2015.
 */
public abstract class PersistenceManager {

    public static  File APPDATA_FOLDER;
    public static  File SETTINGS_FOLDER;
    public static  File STREAMS_FILE;
    public static  File SETTINGS_FILE;
    private String os;
    private  OperatingSystem operatingSystem;

    protected PersistenceManager() throws IOException {
        os = System.getProperty("os.name").toLowerCase();


        if (os.indexOf("win") >= 0) {
            this.setOs(OperatingSystem.Windows);
            APPDATA_FOLDER = new File(System.getenv("APPDATA"));
        } else if (os.indexOf("mac") >= 0) {
            this.setOs(OperatingSystem.Mac);
            APPDATA_FOLDER = new File(System.getProperty("user.home"));
        } else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") >= 0) {
            this.setOs(OperatingSystem.Linux);
            APPDATA_FOLDER = new File("/home/"+ System.getProperty("user.name")
            );
        } else {
            APPDATA_FOLDER = new File("");
            throw new UnsupportedOperationException();
        }

        if(!APPDATA_FOLDER.exists())
        {
            APPDATA_FOLDER.mkdir();
        }

        SETTINGS_FOLDER = new File(APPDATA_FOLDER.getAbsolutePath() + "/.Streamchecker/");
        if (!SETTINGS_FOLDER.exists()) {
            SETTINGS_FOLDER.mkdir();
        }

        STREAMS_FILE =  new File(SETTINGS_FOLDER.getAbsolutePath() + "/Streams/");
        if (!STREAMS_FILE.exists()) {
            STREAMS_FILE.createNewFile();
        }

        SETTINGS_FILE = new File(SETTINGS_FOLDER.getAbsolutePath() + "/Settings/");
        if (!SETTINGS_FILE.exists()) {
            SETTINGS_FILE.createNewFile();
        }


        if (!SETTINGS_FILE.exists()) {
            SETTINGS_FILE.createNewFile();
        }
    }

    public OperatingSystem getOs() {
        return operatingSystem;
    }

    public void setOs(OperatingSystem os) {
        this.operatingSystem = os;
    }

    public enum OperatingSystem {Windows, Linux, Mac,Unknown}

}
