package com.github.fozruk.streamcheckerguitest;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.log4j.Logger;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;

public class MainWindow extends Application {

    final SystemTray tray = SystemTray.getSystemTray();

    private static final Logger logger = Logger.getLogger(AddChannelForm.class);


    public static void startMainWindow(){
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        createTrayIcon(primaryStage);
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL location = getClass().getResource("/sample.fxml");
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        Parent root =   fxmlLoader.load(location.openStream());
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("XDDDDDDDDDDDDDDDDDDDDDDD");
        Scene scene = new Scene(root,300,700);
        String css = this.getClass().getResource("/test.css").toExternalForm();
        scene.getStylesheets().add(css);
        primaryStage.setScene(scene);
        primaryStage.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                logger.trace("Focus event " + observable + " " + oldValue + " " + newValue);
                if(!newValue)
                {
                  primaryStage.hide();
                }
            }
        });



        //Workaround for TrayIcon, after calling primaryStage.hide(), the JavaFx Thread terminates and the window cant be shown again. This prevents the Thread to close.

        Stage dummyPopup = new Stage();
        dummyPopup.initModality(Modality.NONE);
// set as utility so no iconification occurs
        dummyPopup.initStyle(StageStyle.UTILITY);
// set opacity so the window cannot be seen
        dummyPopup.setOpacity(0d);
// not necessary, but this will move the dummy stage off the screen
        final Screen screen = Screen.getPrimary();
        final Rectangle2D bounds = screen.getVisualBounds();
        dummyPopup.setX(bounds.getMaxX());
        dummyPopup.setY(bounds.getMaxY());
// create/add a transparent scene
        final Group root2 = new Group();
        dummyPopup.setScene(new Scene(root2, 1d, 1d, Color.TRANSPARENT));
// show the dummy stage
        dummyPopup.show();
    }

    public void createTrayIcon(Stage stage) throws IOException, AWTException {
        Image icon = ImageIO.read(MainWindow.class.getResourceAsStream("/pictures/logo.png"));
        TrayIcon trayIcon = new TrayIcon(icon);
        trayIcon.setImageAutoSize(true);
        tray.add(trayIcon);
        trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO Auto-generated method stub
                super.mouseClicked(e);
                logger.debug("dddd");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        logger.debug("Gonna show MainWindow, in thread");

                        Dimension scrnSize = Toolkit.getDefaultToolkit().getScreenSize();
                        Rectangle winSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();

                        int taskBarHeight = scrnSize.height - winSize.height + 10;
                        int dektopheight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
                        int desktopwidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
                        int fensterhoehe = (int) stage.getHeight();
                        int locationY = (dektopheight) - (fensterhoehe + taskBarHeight) ;
                        int locationX = (int) MouseInfo.getPointerInfo().getLocation().getX();
                        if(locationX+stage.getWidth() > desktopwidth)
                            locationX = (int) (desktopwidth - stage.getWidth());
                        stage.setX(locationX);
                        stage.setY(locationY);
                        stage.show();
                    }
                });
            }
        });
    }
}
