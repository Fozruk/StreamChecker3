package com.github.fozruk.streamcheckerguitest.settings;

import com.github.fozruk.streamcheckerguitest.plugins.base.PluginLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Philipp on 10.10.2015.
 */
public class StreamPlatformSettings extends VBox implements Initializable {

    @FXML
    private TextField usernamee;

    @FXML
    private PasswordField password;

    public Class<? extends PluginLoader> getPlatformName() {
        return platformName;
    }

    public void setPlatformName(Class<? extends PluginLoader> platformName) {
        this.platformName = platformName;
    }

    private Class<? extends PluginLoader> platformName;

    public StreamPlatformSettings(Class<? extends PluginLoader> loader)
    {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/fxml/chatSettings.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        this.platformName = loader;

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private Class<? extends PluginLoader> streamPlugin;

    public Class<? extends PluginLoader> getStreamPlugin() {
        return streamPlugin;
    }

    public void setStreamPlugin(Class<? extends PluginLoader> streamPlugin) {
        this.streamPlugin = streamPlugin;
    }

    public CharSequence getPassword() {
        return password.getCharacters();
    }

    public void setPassword(String password) {
        this.password.setText(password);
    }

    public String getUsername() {
        return usernamee.getText();
    }

    public void setUsername(String username) {
        this.usernamee.setText(username);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
