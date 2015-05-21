package com.github.fozruk.streamcheckerguitest;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {



    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));

        primaryStage.setTitle("XDDDDDDDDDDDDDDDDDDDDDDD");
        primaryStage.setScene(new Scene(root, 300, 275));



/*        VBox grid = new VBox();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(25,25,25,25));

        Scene scene = new Scene(grid,300,275);
        primaryStage.setScene(scene);

        Text screentitle = new Text("XDDDDDD  DAELE PW XDDDDDDDDD NO SCAM XDDDDD");
        screentitle.setFont(Font.font("Tahoma", FontWeight.NORMAL,20));


        Label userName = new Label("User Name:");

        TextField userTextField = new TextField();

        Label pw = new Label("Password:");

        PasswordField pwBox = new PasswordField();

        grid.getChildren().addAll(screentitle,userName,userTextField,pw,pwBox);


        grid.setStyle("-fx-border-color: blue;-fx-border-style: solid;-fx- 45 46 border-width: 5;");*/

        primaryStage.show();
    }




    public static void main(String[] args) {
        launch(args);
    }
}
