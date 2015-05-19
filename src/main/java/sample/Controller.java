package sample;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

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



    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert listView != null : "fx:id=\"myButton\" was not injected: check your FXML file 'simple.fxml'.";

        assert modalMenu_backButton != null : "fx:id=\"myButton\" was not injected: check your FXML file 'simple.fxml'.";

        ObservableList<String> list = FXCollections.observableArrayList("Yolo", "Sweck");
        listView.setItems(list);

        modalMenu_backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                logger.trace(modalMenu_backButton + " click event triggered.");
                TranslateTransition ft = new TranslateTransition(Duration.millis(250), modalMenuGrid);
                ft.setByY(top.getHeight());
                ft.play();

                blurBox.setEffect(null);


            }
        });

        settingsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                logger.trace(modalMenu_backButton + " click event triggered.");
                TranslateTransition ft = new TranslateTransition(Duration.millis(250), modalMenuGrid);
                ft.setByY(-top.getHeight());
                ft.play();

                blurBox.setEffect(new GaussianBlur(12));
            }
        });


    }
}
