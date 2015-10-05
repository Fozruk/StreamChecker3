package com.github.fozruk.streamcheckerguitest.experimental;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import javax.swing.*;

/**
 * Created by Philipp on 12.08.2015.
 */
public class MediaPlayground {

    static MediaView view;
    static JFXPanel jfxPanel;

    public static void main(String[] args)
    {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                initAndShowGUI();
            }
        });


    }

    private static void initAndShowGUI()
    {
        Media media = new Media("http://127.0.0.1:51370/");
        MediaPlayer player = new MediaPlayer(media);
        player.play();
        view = new MediaView(player);


        JFrame frame = new JFrame();
        jfxPanel = new JFXPanel();



        frame.add(jfxPanel);
        frame.setVisible(true);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                initFX();

            }
        });

    }

    private static void initFX() {
        Scene scene = createScene();
        jfxPanel.setScene(scene);


    }

    private static Scene createScene() {
        Group root  =  new  Group();
        Scene  scene  =  new  Scene(root, Color.ALICEBLUE);
        Text text  =  new  Text();

        text.setX(40);
        text.setY(100);
        text.setFont(new Font(25));
        text.setText("Welcome JavaFX!");

        root.getChildren().add(text);


        ((Group) scene.getRoot()).getChildren().add(view);


        return (scene);
    }

}
