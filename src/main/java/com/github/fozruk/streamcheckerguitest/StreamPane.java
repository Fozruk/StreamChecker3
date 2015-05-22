package com.github.fozruk.streamcheckerguitest;

import com.github.epilepticz.streamchecker.model.channel.interf.IChannel;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Created by philipp.hentschel on 19.05.15.
 */
public class StreamPane extends AnchorPane {

    private static final Logger logger = Logger.getLogger(StreamPane.class);

    @FXML
    private Label name;

    @FXML
    private Label viewers;

    @FXML
    private Label uptime;

    @FXML
    private VBox panes;

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
    }

    public void updateLabels()
    {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                StreamPane.this.name.setText(channel.getChannelLink());
                StreamPane.this.uptime.setText("00:00:00");
                StreamPane.this.viewers.setText(String.valueOf(channel.getViewerAmount()));

            }
        });
    }

    public IChannel getChannel() {
        return channel;
    }
}
