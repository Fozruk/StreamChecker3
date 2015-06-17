package com.github.fozruk.streamcheckerguitest;

import com.github.epilepticz.JavaLivestreamerWrapper.ILivestreamerObserver;
import com.github.epilepticz.JavaLivestreamerWrapper.LivestreamerWrapper;
import com.github.epilepticz.JavaLivestreamerWrapper.SortOfMessage;
import com.github.epilepticz.streamchecker.model.channel.interf.IChannel;
import com.github.fozruk.streamcheckerguitest.exception.PropertyKeyNotFoundException;
import com.github.fozruk.streamcheckerguitest.persistence.PersistedSettingsManager;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import org.apache.log4j.Logger;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by philipp.hentschel on 19.05.15.
 */
public class StreamPane extends StackPane implements ILivestreamerObserver {

    private static final Logger logger = Logger.getLogger(StreamPane.class);

    @FXML
    private Label name;

    @FXML
    private Label viewers;

    @FXML
    private Label uptime;

    @FXML
    private GridPane panes;

    @FXML
    private Label isOnline;

    @FXML
    private StackPane anchor;

    @FXML
    private ImageView imagelol;

    @FXML
    private Button deleteButton;

    @FXML
    private Button watchButton;

    @FXML
    private Button chatButton;

    @FXML
    private Button watchVlc;

    @FXML
    private Button watchBrowser;

    @FXML
    private HBox buttonBox;

    @FXML
    private HBox buttonBox1;

    @FXML
    private StackPane stackPane;

    private IChannel channel;

    public StreamPane(IChannel channel)
    {

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/fxml/Stream.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);


        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.channel = channel;

        updateLabels();


        panes.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            }
        });

        anchor.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                logger.trace("Mouse event");
                if(!channel.isOnline())
                    watchButton.setDisable(true);
                else
                    watchButton.setDisable(false);
                panes.setEffect(new GaussianBlur(20));
                stackPane.setVisible(true);
            }
        });

        anchor.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                logger.trace("Mouse event");
                panes.setEffect(null);
                buttonBox.setVisible(true);
                buttonBox1.setVisible(false);
                stackPane.setVisible(false);
            }
        });

        watchButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                logger.trace("Mouse Event");
                buttonBox.setVisible(false);
                buttonBox1.setVisible(true);

            }
        });

        watchVlc.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                PersistedSettingsManager manager = Controller.getCurrentController().getSettingsManager();
                LivestreamerWrapper wrapper = new LivestreamerWrapper(manager.getLivestremer(),manager.getVideoPlayer());
                wrapper.addObserver(StreamPane.this);

                    wrapper.startLivestreamerWithURL(new URL(channel.getChannelLink()),"source");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (PropertyKeyNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        watchBrowser.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Desktop desktop = java.awt.Desktop.getDesktop();
                URI oURL = null;
                try {
                    oURL = new URI(channel.getChannelLink());
                    desktop.browse(oURL);
                } catch (URISyntaxException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        deleteButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    Controller.getCurrentController().deleteChannel(channel);
                    Controller.getCurrentController().getChannelPersistanceManager().deleteChannel();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        if(channel.getChannelLink().toLowerCase().contains("twitch"))
        {
            imagelol.setImage(new Image(StreamPane.class.getResourceAsStream("/pictures/twitch.png")));

        } else
        {
            imagelol.setImage(new Image(StreamPane.class.getResourceAsStream("/pictures/hitbox.png")));

        }

        chatButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Desktop desktop = java.awt.Desktop.getDesktop();
                URI oURL = null;
                try {
                    oURL = new URI(channel.getChannelLink() + "/chat?popout=");
                    desktop.browse(oURL);
                } catch (URISyntaxException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

    }

    public void updateLabels()
    {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                StreamPane.this.name.setText(channel.getChannelName());
                StreamPane.this.name.setMinWidth(Region.USE_PREF_SIZE);
                StreamPane.this.name.setMaxWidth(Region.USE_PREF_SIZE);
                StreamPane.this.uptime.setText(channel.getUptime());
                StreamPane.this.viewers.setText(String.valueOf(channel.getViewerAmount()));
                StreamPane.this.isOnline.setText(channel.isOnline() ? "Online" : "Offline");
                StreamPane.this.isOnline.setStyle( (!channel.isOnline()?"-fx-text-fill: red;":"-fx-text-fill: #00e005"));

            }
        });
    }

    public IChannel getChannel() {
        return channel;
    }

    @Override
    public void recieveLivestreamerMessage(String message, SortOfMessage sort) {
        logger.debug("XDDDD " + message + " " + sort.name());
    }



}

