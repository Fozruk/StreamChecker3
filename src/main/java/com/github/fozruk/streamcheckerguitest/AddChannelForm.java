package com.github.fozruk.streamcheckerguitest;

import com.github.epilepticz.streamchecker.exception.CreateChannelException;
import com.github.epilepticz.streamchecker.model.channel.impl.HitboxTVChannel;
import com.github.epilepticz.streamchecker.model.channel.impl.TwitchTVChannel;
import com.github.epilepticz.streamchecker.model.channel.interf.IChannel;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Philipp on 23.05.2015.
 */
public class AddChannelForm extends StackPane implements Initializable {

    private static final Logger logger = Logger.getLogger(AddChannelForm.class);

    ;
    private Channel channeltype;
    @FXML
    private ImageView image;
    @FXML
    private Button addButton;
    @FXML
    private Button cancelButton;
    @FXML
    private GridPane grid;
    @FXML
    private StackPane stackpane;
    @FXML
    private TextField inputField;

    public AddChannelForm(Channel type) {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/fxml/addChannelForm.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        channeltype = type;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        image.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                logger.trace("Entered Event");
                if (grid.getOpacity() != 1.0) {
                    mouseOverEffect();
                }
            }
        });

        image.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                logger.trace("Entered Event");
                if (grid.getOpacity() != 1.0) {
                    image.setEffect(null);
                    deleteMouseOverEffect();
                }
            }
        });

        image.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mousePresed();
            }
        });

        image.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mouseReleased();
                fadeIn(grid);
                fadeout(image);
                image.setVisible(false);
            }
        });

        cancelButton.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                logger.trace("CancelButton event fired");
                image.setVisible(true);
                fadeout(grid);
                fadeIn(image);
            }
        });

        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // logger.trace(modalMenu_backButton + " click event triggered.");
                try {

                    switch (channeltype) {
                        case Hitbox:
                            IChannel hitboxChannel = new HitboxTVChannel(AddChannelForm.this.inputField.getText());
                            Controller.getCurrentController().createChannel(hitboxChannel);
                            Controller.getCurrentController().getChannelPersistanceManager().saveChannel(hitboxChannel);
                            break;

                        case Twitch:
                            IChannel channel = new TwitchTVChannel(AddChannelForm.this.inputField.getText());
                            Controller.getCurrentController().createChannel(channel);
                            Controller.getCurrentController().getChannelPersistanceManager().saveChannel(channel);
                            break;
                    }


                } catch (CreateChannelException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    AddChannelForm.this.inputField.setText("");
                    image.setVisible(true);
                    image.setOpacity(0.5);
                    fadeout(grid);
                    Button btn = (Button) AddChannelForm.this.getParent().lookup("#addChannelBack");
                    btn.fire();
                }
            }
        });

        // this.stackpane.setAlignment(Pos.CENTER);
    }

    private void mouseOverEffect() {
        Effect effect = new DropShadow(10, Color.BLACK);
        image.setEffect(effect);

        FadeTransition ft = new FadeTransition(Duration.millis(100), image);
        ft.setToValue(1.0);
        ft.play();
    }

    private void deleteMouseOverEffect() {
        Effect effect = new DropShadow(10, Color.BLACK);
        image.setEffect(effect);

        FadeTransition ft = new FadeTransition(Duration.millis(100), image);
        ft.setToValue(0.4);
        ft.play();
    }

    private void mousePresed() {
        ScaleTransition tr = new ScaleTransition(Duration.millis(100), image);
        tr.setToZ(0.8);
        tr.setToY(0.8);
        tr.setToX(0.8);
        tr.play();
    }

    private void mouseReleased() {
        ScaleTransition tr = new ScaleTransition(Duration.millis(100), image);
        tr.setToZ(1.0);
        tr.setToY(1.0);
        tr.setToX(1.0);
        tr.play();
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

    public ImageView getImage() {
        return image;
    }

    public static enum Channel {Twitch, Hitbox}
}
