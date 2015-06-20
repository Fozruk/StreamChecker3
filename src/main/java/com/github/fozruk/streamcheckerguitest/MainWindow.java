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



    private static final Logger logger = Logger.getLogger(AddChannelForm.class);
    private static TrayIcon trayIcon;
    private static Stage primaryStage;

    public static void startMainWindow(){
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        javax.swing.SwingUtilities.invokeLater(this::createTrayIcon);
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL location = Main.class.getClass().getResource("/fxml/sample.fxml");
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        Parent root =   fxmlLoader.load(location.openStream());
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("XDDDDDDDDDDDDDDDDDDDDDDD");
        Scene scene = new Scene(root,300,700);
        String css = this.getClass().getResource("/test.css").toExternalForm();
        scene.getStylesheets().add(css);
        primaryStage.setScene(scene);
        Platform.setImplicitExit(false);
        primaryStage.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                logger.trace("Focus event " + observable + " " + oldValue + " " + newValue);
                if (!newValue) {
                    primaryStage.hide();
                }
            }
        });
    }

    public void createTrayIcon(){
        try
        {
            java.awt.Toolkit.getDefaultToolkit();

            // app requires system tray support, just exit if there is no support.
            if (!java.awt.SystemTray.isSupported()) {
                System.out.println("No system tray support, application exiting.");
                Platform.exit();
            }

            SystemTray tray = SystemTray.getSystemTray();
            Image icon = ImageIO.read(MainWindow.class.getResourceAsStream("/pictures/logo.png"));
            trayIcon = new TrayIcon(icon);
            trayIcon.setImageAutoSize(true);
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
                            Platform.runLater(MainWindow.this::showStage);
                        }
                    });
                }
            });

            logger.debug("Add TrayIcon to tray");
            tray.add(trayIcon);

        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void showStage()
    {
        Dimension scrnSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle winSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        int taskBarHeight = scrnSize.height - winSize.height + 10;
        int dektopheight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        int desktopwidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int fensterhoehe = (int) primaryStage.getHeight();
        int locationY = (dektopheight) - (fensterhoehe + taskBarHeight);
        int locationX = (int) MouseInfo.getPointerInfo().getLocation().getX();
        if (locationX + primaryStage.getWidth() > desktopwidth)
            locationX = (int) (desktopwidth - primaryStage.getWidth());
        primaryStage.setX(locationX);
        primaryStage.setY(locationY);
        primaryStage.show();
    }
    //Some help maybe https://gist.github.com/jewelsea/e231e89e8d36ef4e5d8a
    public static void showMessage(String info, String message) {
        logger.debug("Balloob Tip: " + message);
        //javax.swing.SwingUtilities.invokeLater(() -> trayIcon.displayMessage("h", "k", TrayIcon.MessageType.ERROR));
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                logger.debug("This should be in AWT Event Queue");
                trayIcon.displayMessage("Info",message, TrayIcon.MessageType.INFO);
            }
        });
    }


    public static Stage getPrimaryStage() {
        return primaryStage;
    }




}
