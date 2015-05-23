package com.github.fozruk.streamcheckerguitest;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;

public class MainWindow extends Application {

    public static void startMainWindow(){
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL location = getClass().getResource("/sample.fxml");
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        Parent root =   fxmlLoader.load(location.openStream());
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("XDDDDDDDDDDDDDDDDDDDDDDD");
        Scene scene = new Scene(root,300,600);
        String css = this.getClass().getResource("/test.css").toExternalForm();
        scene.getStylesheets().add(css);
        primaryStage.setScene(scene);

          primaryStage.show();
    }
}
