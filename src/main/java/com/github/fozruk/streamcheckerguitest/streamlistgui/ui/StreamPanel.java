package com.github.fozruk.streamcheckerguitest.streamlistgui.ui;

import com.github.epilepticz.JavaLivestreamerWrapper.ILivestreamerObserver;
import com.github.epilepticz.JavaLivestreamerWrapper.LivestreamerWrapper;
import com.github.epilepticz.JavaLivestreamerWrapper.SortOfMessage;
import com.github.epilepticz.JavaLivestreamerWrapper.StdinLivestreamer;
import com.github.epilepticz.streamchecker.exception.CreateChannelException;
import com.github.epilepticz.streamchecker.model.channel.interf.IChannel;
import com.github.fozruk.streamcheckerguitest.persistence.PersistedSettingsManager;
import com.github.fozruk.streamcheckerguitest.streamlistgui.controller.Controller;
import com.github.fozruk.streamcheckerguitest.exception.PropertyKeyNotFoundException;
import com.github.fozruk.streamcheckerguitest.vlcgui.controller.VlcLivestreamController;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by philipp.hentschel on 19.05.15.
 */
public class StreamPanel extends StackPane implements ILivestreamerObserver {

    private static final Logger logger = LoggerFactory.getLogger(StreamPanel.class);

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

    @FXML
    private Button vlcButton;

    private IChannel channel;

    public StreamPanel(IChannel channel) {

        loadFxml();
        this.channel = channel;
        watchButton.setDisable(false);

        updateLabels();


        panes.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            }
        });

        addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                logger.trace("Mouse event");
                //if (!channel.isOnline())
                    //watchButton.setDisable(true);
                //else
                    //watchButton.setDisable(false);
                panes.setEffect(new GaussianBlur(20));
                stackPane.setVisible(true);
            }
        });

        addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
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
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            PersistedSettingsManager
                            manager = PersistedSettingsManager.getInstance();
                            manager.getVideoPlayerPath();
                            manager.getLivestreamerPath();
                            VlcLivestreamController gui = new VlcLivestreamController
                                    (channel);


                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (PropertyKeyNotFoundException e) {
                            JOptionPane.showMessageDialog(null, "Please type " +
                                            "in valid paths for livestreamer " +
                                            "and vlc", "Settings missing.",
                                    JOptionPane.ERROR_MESSAGE);
                        } catch (CreateChannelException e) {
                            e.printStackTrace();
                        } finally {
                            Controller.getCurrentController().hideWindow();
                        }
                    }
                });
                thread.setName("VLC Thread - " + channel.getChannelName());
                thread.start();
            }
        });

        vlcButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    PersistedSettingsManager
                            manager = PersistedSettingsManager.getInstance();
                    new StdinLivestreamer(manager.getLivestreamerPath(), manager.getVideoPlayerPath())
                            .startLivestreamerWithURL(new URL(channel
                                    .getChannelLink()),
                            "best");
                } catch (PropertyKeyNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
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
                } finally {
                    Controller.getCurrentController().hideWindow();
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

        if (channel.getChannelLink().toLowerCase().contains("twitch")) {
            imagelol.setImage(new Image(StreamPanel.class.getResourceAsStream("/pictures/twitch.png")));

        } else {
            imagelol.setImage(new Image(StreamPanel.class.getResourceAsStream("/pictures/hitbox.png")));

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

    private synchronized void loadFxml() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/fxml/streamComponent.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void updateLabels() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                StreamPanel.this.name.setText(channel.getChannelName());
                StreamPanel.this.name.setMinWidth(Region.USE_PREF_SIZE);
                StreamPanel.this.name.setMaxWidth(Region.USE_PREF_SIZE);
                StreamPanel.this.uptime.setText(channel.getUptime());
                StreamPanel.this.viewers.setText(String.valueOf(channel.getViewerAmount()));
                StreamPanel.this.isOnline.setText(channel.isOnline() ? "Online" : "Offline");
                StreamPanel.this.isOnline.setStyle((!channel.isOnline() ?
                        "-fx-text-fill: #d18080;" : "-fx-text-fill: #80d181"));

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

