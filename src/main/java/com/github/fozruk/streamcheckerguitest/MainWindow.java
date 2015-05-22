package com.github.fozruk.streamcheckerguitest;

import com.github.epilepticz.streamchecker.controller.StreamcheckerController;
import com.github.epilepticz.streamchecker.view.interf.IOverview;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

public class MainWindow extends Application {

    private static Controller controller;
    public MainWindow()
    {

    }

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
        primaryStage.setTitle("XDDDDDDDDDDDDDDDDDDDDDDD");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    public static IOverview getController() {
        return controller;
    }

}
