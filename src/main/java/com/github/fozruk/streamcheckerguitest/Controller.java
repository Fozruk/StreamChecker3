package com.github.fozruk.streamcheckerguitest;

import com.github.epilepticz.streamchecker.controller.StreamcheckerController;
import com.github.epilepticz.streamchecker.exception.CreateChannelException;
import com.github.epilepticz.streamchecker.exception.NoSuchChannelViewInOverviewException;
import com.github.epilepticz.streamchecker.model.channel.impl.TwitchTVChannel;
import com.github.epilepticz.streamchecker.model.channel.interf.IChannel;
import com.github.epilepticz.streamchecker.view.interf.IOverview;
import com.github.fozruk.StreamPane.StreamPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.apache.log4j.Logger;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable , IOverview {

    private static final Logger logger = Logger.getLogger(Controller.class);

    @FXML
    private ListView listView;

    @FXML
    private Button modalMenu_backButton;

    @FXML
    private GridPane modalMenuGrid;

    @FXML
    private GridPane top;

    @FXML
    private VBox blurBox;

    @FXML
    private Button settingsButton;

    @FXML
    private Parent root;

    private ObservableList<StreamPane> list;

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {

        list = FXCollections.observableArrayList();
        listView.setItems(list);
        modalMenu_backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {


            }
        });

        settingsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                logger.trace(modalMenu_backButton + " click event triggered.");
//                TranslateTransition ft = new TranslateTransition(Duration.millis(250), modalMenuGrid);
//                ft.setByY(-modalMenu_backButton.getHeight());
//                ft.play();
//
//                blurBox.setEffect(new GaussianBlur(12));

                try {
                    Main.controller.createChannel(new TwitchTVChannel("rocketbeanstv"));
                } catch (CreateChannelException e) {
                    e.printStackTrace();
                }
            }
        });

        Main.controller = new StreamcheckerController(this);
    }


    @Override
    public void addChannel(IChannel channel) {
        StreamPane pane = new StreamPane(channel.getChannelLink());
        list.add(pane);
    }

    @Override
    public void updateDataInChannelViewFor(IChannel channel) {
        list.get(0).setText(channel.getChannelLink() + " " + String.valueOf(channel.isOnline()));
    }

    @Override
    public void deleteChannelViewFor(IChannel channel) throws NoSuchChannelViewInOverviewException {

    }

    @Override
    public IChannel[] getAddedChannels() {
        return new IChannel[0];
    }

    @Override
    public void errorCodeChangedFor(IChannel channel, int errorcount) {

    }

}
