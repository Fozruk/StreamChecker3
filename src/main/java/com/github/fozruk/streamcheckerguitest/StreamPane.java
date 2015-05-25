package com.github.fozruk.streamcheckerguitest;

import com.github.epilepticz.streamchecker.model.channel.interf.IChannel;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Created by philipp.hentschel on 19.05.15.
 */
public class StreamPane extends StackPane {

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

    private IChannel channel;

    public StreamPane(IChannel channel)
    {

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/Stream.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);


        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.channel = channel;

        panes.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                logger.trace("Mouse Event");
                Main.controller.deleteChannel(channel);
            }
        });

        if(channel.getChannelLink().toLowerCase().contains("twitch"))
        {
            imagelol.setImage(new Image("pictures\\twitchIcon.png"));
            imagelol.setRotate(0);

        } else
        {
            imagelol.setImage(new Image("pictures\\hitboxIcon.png"));

        }

    }

    public void updateLabels()
    {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                StreamPane.this.name.setText(channel.getChannelName());
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
}
