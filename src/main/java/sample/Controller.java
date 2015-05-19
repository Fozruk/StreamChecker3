package sample;

import com.github.fozruk.StreamPane.StreamPane;
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
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.log4j.Logger;
import javafx.stage.WindowEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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

    @FXML
    private Parent root;


    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {

        List s = new ArrayList<StreamPane>();
        for(int i = 0; i<20;i++)
            s.add(new StreamPane(String.valueOf(i)));

        ObservableList<StreamPane> list = FXCollections.observableArrayList(s);
        listView.setItems(list);



        modalMenu_backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                logger.trace(modalMenu_backButton + " click event triggered.");
                TranslateTransition ft = new TranslateTransition(Duration.millis(250), modalMenuGrid);
                ft.setByY(modalMenuGrid.getHeight());
                ft.play();

                blurBox.setEffect(null);


            }
        });

        settingsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                logger.trace(modalMenu_backButton + " click event triggered.");
                TranslateTransition ft = new TranslateTransition(Duration.millis(250), modalMenuGrid);
                ft.setByY(-modalMenu_backButton.getHeight());
                ft.play();

                blurBox.setEffect(new GaussianBlur(12));
            }
        });



    }


}
