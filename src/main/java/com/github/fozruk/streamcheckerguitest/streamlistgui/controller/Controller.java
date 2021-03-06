package com.github.fozruk.streamcheckerguitest.streamlistgui.controller;

import com.github.epilepticz.streamchecker.controller.StreamcheckerController;
import com.github.epilepticz.streamchecker.exception.NoSuchChannelViewInOverviewException;
import com.github.epilepticz.streamchecker.model.channel.impl.AbstractChannel;
import com.github.epilepticz.streamchecker.model.channel.impl.HitboxTVChannel;
import com.github.epilepticz.streamchecker.model.channel.impl.TwitchTVChannel;
import com.github.epilepticz.streamchecker.model.channel.interf.IChannel;
import com.github.epilepticz.streamchecker.model.channel.interf.IChannelobserver;
import com.github.epilepticz.streamchecker.view.interf.IOverview;
import com.github.fozruk.streamcheckerguitest.plugins.base.PluginLoader;
import com.github.fozruk.streamcheckerguitest.settings.StreamPlatformSettings;
import com.github.fozruk.streamcheckerguitest.streamlistgui.ui.AddChannelForm;
import com.github.fozruk.streamcheckerguitest.streamlistgui.ui.StreamListUI;
import com.github.fozruk.streamcheckerguitest.streamlistgui.ui.StreamPanel;
import com.github.fozruk.streamcheckerguitest.exception.PropertyKeyNotFoundException;
import com.github.fozruk.streamcheckerguitest.persistence.PersistedChannelsManager;
import com.github.fozruk.streamcheckerguitest.persistence.PersistedSettingsManager;
import com.github.fozruk.streamcheckerguitest.vlcgui.ui.FileChooser;
import com.google.common.collect.ComparisonChain;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;

public class Controller implements Initializable, IOverview, IChannelobserver {

    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    private static Controller currentInstance;
    @FXML
    private ListView listView;
    @FXML
    private GridPane modalMenuGrid;
    @FXML
    private GridPane top;
    @FXML
    private VBox blurBox;
    @FXML
    private GridPane grid;
    @FXML
    private Button settingsButton;
    @FXML
    private Button addButton;
    @FXML
    private Parent root;
    @FXML
    private Button addChannelBack;
    @FXML
    private Button exitButton;
    @FXML
    private Label label;
    @FXML
    private GridPane settings;

    //Settings buttons
    @FXML
    private Button settingsBack;
    @FXML
    private Button settingsOk;
    @FXML
    private Button settingsLivestreamer;
    @FXML
    private Button settingsVlc;
    @FXML
    private TextField settingsTextfiledVlc;
    @FXML
    private TextField settingsTextfiledLivestreamer;

    @FXML
    private TabPane tabPane;

    private ObservableList<StreamPanel> list;
    private PersistedSettingsManager settingsManager;
    private PersistedChannelsManager channelPersistanceManager;
    private StreamcheckerController controller;
    private List<StreamPlatformSettings> chatSettingsList = new ArrayList<>();
    private PersistedSettingsManager manager;

    public static Controller getCurrentController() {
        return currentInstance;
    }

    public static void setCurrentController(Controller con) {
        Controller.currentInstance = con;
    }

    @Override
    // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {

        try {
            this.manager = PersistedSettingsManager
                    .getInstance();
        } catch (IOException e) {
            e.printStackTrace();
        }
        list = FXCollections.observableArrayList();

        settingsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Controller.this.listView.setEffect(new GaussianBlur(30.0));
                fadeIn(settings);
                fadeOut(grid);

                try {
                    settingsTextfiledLivestreamer.setText(PersistedSettingsManager.getInstance().getLivestreamerPath());
                    settingsTextfiledVlc.setText(PersistedSettingsManager.getInstance().getVideoPlayerPath());
                    loadChannels();
                } catch (PropertyKeyNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        list.addListener(new ListChangeListener<StreamPanel>() {
            @Override
            public void onChanged(Change<? extends StreamPanel> c) {
            }
        });

        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                fadeOut(settings);
                Controller.this.listView.setEffect(new GaussianBlur(30.0));
                fadeIn(grid);
            }
        });


        addChannelBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                logger.trace("Add AbstractChannel Back Event fired");
                fadeOut(grid);
                System.out.println(grid.isVisible());
                System.out.println(settings.isVisible());
                resetEffect(listView);
            }
        });

        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.exit(1);
            }
        });

        settingsBack.setOnAction((e) -> {
            fadeOut(settings);
            System.out.println(grid.isVisible());
            System.out.println(settings.isVisible());
            resetEffect(listView);
        });

        settingsOk.setOnAction((e) -> {
            try {
                saveSettings();
                fadeOut(settings);
                resetEffect(listView);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        //TODO make platform independent
        settingsLivestreamer.setOnAction((e) -> {
            SwingUtilities.invokeLater(()->{
                FileChooser chooser = new FileChooser("livestreamer.exe");
                File file = chooser.getLocationWithFileChooser();
                if(file != null)
                    settingsTextfiledLivestreamer.setText(file.getAbsolutePath());
            });
        });

        settingsVlc.setOnAction((e) -> {
            SwingUtilities.invokeLater(()->
            {
                FileChooser chooser = new FileChooser("vlc.exe");
                File file = chooser.getLocationWithFileChooser();
                if(file != null)
                    settingsTextfiledVlc.setText(file.getAbsolutePath());
            });
        });

        //Add Streamplatform plugins

        AddChannelForm form = new AddChannelForm(TwitchTVChannel.class.getCanonicalName());
        form.getImage().setImage(new Image(Controller.class.getResourceAsStream("/pictures/twitch-logo-black.png")));

        AddChannelForm form2 = new AddChannelForm(HitboxTVChannel.class.getCanonicalName());
        form2.getImage().setImage(new Image(Controller.class.getResourceAsStream("/pictures/hitboxlogogreen.png")));
        grid.add(form, 0, 1);
        grid.add(form2, 0, 2);


        label.setTextFill(Paint.valueOf("#5e5e5e"));

        this.controller = new StreamcheckerController(this);
        currentInstance = this;

        //Load all persisted Channels
        //TODO generischer machen
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    loadChannels2();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void loadChannels2() throws JSONException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException, IOException {
        // settingsManager = PersistedSettingsManager.getInstance();
        channelPersistanceManager = new PersistedChannelsManager();
        logger.debug("Load Persisted Channels...");
        JSONObject channel = channelPersistanceManager
                .loadPersistedChannels();

        Iterator keys = channel.keys();
        while(keys.hasNext())
        {
            String key = (String) keys.next();
            JSONArray chatimplclasspath = channel.getJSONArray(key);
            Class impl = Class.forName(key);
            for(int i =0;i<chatimplclasspath.length();i++)
            {
                createChannel((AbstractChannel) impl.getDeclaredConstructor
                        (String.class).newInstance(chatimplclasspath.get(i)));
            }
        }
        Platform.runLater(() -> list.sort(comparator));
        listView.setItems(list);
        loadChannels();
    }

    private void saveSettings() throws IOException {
        manager.saveSetting("livestreamer",settingsTextfiledLivestreamer.getText());
        manager.saveSetting("videoPlayer",settingsTextfiledVlc.getText());
        chatSettingsList.forEach((e) -> {
            try {
                System.out.println(e.getUsername());
                String name = e.getUsername();
                CharSequence pw = e.getPassword();
                if (!(name == null || pw == null)) {
                    String classname = e.getPlatformName().getSimpleName()
                            .replace("Channel_Gui", "");
                    manager.saveSetting(classname+".username", name);
                    manager.saveSetting(classname+".token", pw.toString());
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        manager.flush();
    }

    public void resetEffect(Node node)
    {
        node.setEffect(null);
    }

    public void createChannel(IChannel abstractChannel) {
        abstractChannel.addObserver(this);
        controller.createChannel(abstractChannel);
    }

    public void deleteChannel(IChannel channel) {
        channel.removeObserver(this);
        controller.deleteChannel(channel);
    }

    @Override
    public void addChannel(IChannel channel) {
        StreamPanel pane = new StreamPanel(channel);
                list.add(pane);
    }

    @Override
    public void updateDataInChannelViewFor(IChannel channel) {
        for (StreamPanel channelObject : list) {
            if (channel.equals(channelObject.getChannel())) {
                channelObject.updateLabels();
                break;
            }
        }
    }

    @Override
    public void deleteChannelViewFor(IChannel channel) throws NoSuchChannelViewInOverviewException {
        for (StreamPanel channelObject : list) {
            if (channel.equals(channelObject.getChannel())) {
                list.remove(channelObject);
                return;
            }
        }
        throw new NoSuchChannelViewInOverviewException();
    }

    @Override
    public IChannel[] getAddedChannels() {
        List<IChannel> channels = new ArrayList<>();
        list.forEach((stream) -> channels.add(stream.getChannel()));
        return channels.toArray(new IChannel[0]);
    }

    @Override
    public void errorCodeChangedFor(IChannel channel, int errorcount) {

    }

    private void fadeIn(Node node) {
        node.setVisible(true);
        FadeTransition ft = new FadeTransition(Duration.millis(100), node);
        ft.setToValue(1.0);
        ft.play();
    }



    public void fadeOut(Node node) {
        FadeTransition ft = new FadeTransition(Duration.millis(100), node);
        ft.setToValue(0.0);
        ft.play();
        node.setVisible(false);
    }

    public PersistedSettingsManager getSettingsManager() {
        return settingsManager;
    }


    public PersistedChannelsManager getChannelPersistanceManager() {
        return channelPersistanceManager;
    }

    @Override
    public void recieveNotification(IChannel sender, boolean isOnline) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                list.sort(comparator);
            }
        });
        StreamListUI.ballonManager.addMessageToQueue(sender.getChannelName(),isOnline);
    }

    @Override
    public void receiveStatusString(String message) {

    }

    public void hideWindow() {
        Platform.runLater(() -> StreamListUI.getPrimaryStage().hide());
    }

    private Comparator<StreamPanel> comparator = new Comparator<StreamPanel>() {
        @Override
        public int compare(StreamPanel o1, StreamPanel o2) {
            return ComparisonChain.start().compare(!o1.getChannel()
                    .isOnline(), !o2.getChannel().isOnline()).compare(o1
                    .getChannel().getChannelName().toLowerCase(), o2.getChannel()
                    .getChannelName().toLowerCase())
                    .result();
        }
    };


    //Settings
    private void loadChannels()
    {
        chatSettingsList.clear();
        tabPane.getTabs().clear();
        Reflections reflections = new Reflections("com.github.fozruk.streamcheckerguitest.plugins");
        Set<Class<? extends PluginLoader>> allClasses =
                reflections.getSubTypesOf(PluginLoader.class);

        allClasses.iterator().forEachRemaining(s -> {
            String className = s.getSimpleName().replace("Channel_Gui", "");
            String pw = manager.getValue(className + ".token");
            String username = manager.getValue(className + ".username");
            Tab temp = new Tab();
            temp.setText(className);
            StreamPlatformSettings l = new StreamPlatformSettings(s);
            l.setUsername(username);
            l.setPassword(pw);
            temp.setContent(l);
            Platform.runLater(() -> {
                chatSettingsList.add(l);
                tabPane.getTabs().add(temp);
            });
        });
    }


}
