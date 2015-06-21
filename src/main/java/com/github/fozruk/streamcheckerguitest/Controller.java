package com.github.fozruk.streamcheckerguitest;

import com.github.epilepticz.streamchecker.controller.StreamcheckerController;
import com.github.epilepticz.streamchecker.exception.CreateChannelException;
import com.github.epilepticz.streamchecker.exception.NoSuchChannelViewInOverviewException;
import com.github.epilepticz.streamchecker.model.channel.impl.HitboxTVChannel;
import com.github.epilepticz.streamchecker.model.channel.impl.TwitchTVChannel;
import com.github.epilepticz.streamchecker.model.channel.interf.IChannel;
import com.github.epilepticz.streamchecker.model.channel.interf.IChannelobserver;
import com.github.epilepticz.streamchecker.view.interf.IOverview;
import com.github.fozruk.streamcheckerguitest.persistence.PersistedChannelsManager;
import com.github.fozruk.streamcheckerguitest.persistence.PersistedSettingsManager;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.github.fozruk.streamcheckerguitest.AddChannelForm.Channel;

public class Controller implements Initializable, IOverview, IChannelobserver {

    private static final Logger logger = Logger.getLogger(Controller.class);
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


    private ObservableList<StreamPane> list;
    private PersistedSettingsManager settingsManager;
    private PersistedChannelsManager channelPersistanceManager;
    private StreamcheckerController controller;

    public static Controller getCurrentController() {
        return currentInstance;
    }

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {



        list = FXCollections.observableArrayList();

        settingsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });

        list.addListener(new ListChangeListener<StreamPane>() {
            @Override
            public void onChanged(Change<? extends StreamPane> c) {
            }
        });

        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Controller.this.grid.setVisible(true);
                fadeIn(grid);
            }
        });

        addChannelBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                logger.trace("Add Channel Back Event fired");
                fadeOutAddNewChannel();
            }
        });

        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.exit(1);
            }
        });




        AddChannelForm form = new AddChannelForm(Channel.Twitch);
        form.getImage().setImage(new Image(Controller.class.getResourceAsStream("/pictures/twitch-logo-black.png")));

        AddChannelForm form2 = new AddChannelForm(Channel.Hitbox);
        form2.getImage().setImage(new Image(Controller.class.getResourceAsStream("/pictures/hitboxlogogreen.png")));
        grid.add(form, 0, 1);
        grid.add(form2, 0, 2);


        label.setTextFill(Paint.valueOf("#5e5e5e"));

        listView.setItems(list);


        this.controller = new StreamcheckerController(this);
        this.currentInstance = this;

        //Load all persisted Channels
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    settingsManager = new PersistedSettingsManager();
                    channelPersistanceManager = new PersistedChannelsManager();

                    logger.debug("Load Persisted Channels...");
                    List<String> list = channelPersistanceManager.getPersistedChannels();

                    for (String temp : list) {
                        try {
                            IChannel channel = null;
                            if (temp.toLowerCase().contains("twitch.tv"))
                                createChannel(new TwitchTVChannel(temp.substring(temp.lastIndexOf("/") + 1)));
                            else if (temp.toLowerCase().contains("hitbox.tv"))
                                createChannel(new HitboxTVChannel(temp.substring(temp.lastIndexOf("/")+1)));
                        } catch (CreateChannelException e) {
                            e.printStackTrace();
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();



    }

    public void createChannel(IChannel channel) {
        channel.addObserver(this);
        controller.createChannel(channel);
    }

    public void deleteChannel(IChannel channel) {
        channel.removeObserver(this);
        controller.deleteChannel(channel);
    }

    @Override
    public void addChannel(IChannel channel) {
        StreamPane pane = new StreamPane(channel);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                list.add(pane);
            }
        });

    }

    @Override
    public void updateDataInChannelViewFor(IChannel channel) {
        for (StreamPane channelObject : list) {
            if (channel.equals(channelObject.getChannel())) {
                channelObject.updateLabels();
                break;
            }
        }
    }

    @Override
    public void deleteChannelViewFor(IChannel channel) throws NoSuchChannelViewInOverviewException {
        for (StreamPane channelObject : list) {
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

    private void fadeout(Node node) {
        FadeTransition ft = new FadeTransition(Duration.millis(100), node);
        ft.setToValue(0.0);
        ft.play();
    }

    private void fadeIn(Node node) {
        FadeTransition ft = new FadeTransition(Duration.millis(100), node);
        ft.setToValue(1.0);
        ft.play();
    }

    public void fadeOutAddNewChannel() {
        fadeout(grid);
        Controller.this.grid.setVisible(false);
    }

    public PersistedSettingsManager getSettingsManager() {
        return settingsManager;
    }


    public PersistedChannelsManager getChannelPersistanceManager() {
        return channelPersistanceManager;
    }

    @Override
    public void recieveNotification(IChannel sender, String message) {
        MainWindow.showMessage("Info",message);
    }

    public void hideWindow()
    {
        MainWindow.getPrimaryStage().hide();
    }



}
