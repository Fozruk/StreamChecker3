package com.github.fozruk.streamcheckerguitest.persistence;

import com.github.fozruk.streamcheckerguitest.exception.PropertyKeyNotFoundException;

import java.io.*;
import java.util.Properties;

/**
 * Created by Philipp on 14.06.2015.
 */
public class PersistedSettingsManager extends PersistenceManager {

    Properties settings = new Properties();

    public PersistedSettingsManager() throws IOException {
        loadSettings();
    }

    private void loadSettings() throws IOException {
        String os = System.getProperty("os.name").toLowerCase();

        if(!SETTINGS_FILE.exists())
        {
            SETTINGS_FILE.createNewFile();
        }

        if(os.indexOf("win") >= 0)
        {
            this.setOs(OperatingSystem.Windows);
            loadSettingsWindows();
        } else if(os.indexOf("mac") >= 0)
        {
            this.setOs(OperatingSystem.Mac);
            loadSettingsMac();
        } else if(os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") >= 0)
        {
            this.setOs(OperatingSystem.Linux);
            loadSettingsLinux();
        } else
        {
            throw new UnsupportedOperationException();
        }


    }

    private void loadSettingsWindows() throws IOException {
        BufferedInputStream stream = new BufferedInputStream(new FileInputStream(SETTINGS_FILE));
        settings.load(stream);
        stream.close();
    }

    private void saveSettingsWindows() throws IOException {
        FileOutputStream xddd = new FileOutputStream(SETTINGS_FILE);
        settings.store(xddd,"");
    }

    private void loadSettingsMac() {
        throw new IllegalStateException("Not yet Implemented");
    }

    private void loadSettingsLinux() {
        throw new IllegalStateException("Not yet Umplemented");
    }

    public File getLivestremer() throws PropertyKeyNotFoundException {
        String property = settings.getProperty("livestreamer");
        File livestreamer = null;
        if(property != null)
        {
            livestreamer = new File(property);
        }
        else
        {
            throw new PropertyKeyNotFoundException();
        }
        return livestreamer;
    }

    public File getVideoPlayer() throws PropertyKeyNotFoundException {
        String property = settings.getProperty("videoPlayer");
        File livestreamer = null;
        if(property != null)
        {
            livestreamer = new File(property);
        }
        else
        {
            throw new PropertyKeyNotFoundException();
        }
        return livestreamer;
    }


}
