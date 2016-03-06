package com.github.fozruk.streamcheckerguitest.streamlistgui.ui;

import com.github.fozruk.streamcheckerguitest.BalloonTipManager;
import com.github.fozruk.streamcheckerguitest.Main;
import com.github.fozruk.streamcheckerguitest.persistence.PersistedSettingsManager;
import com.github.fozruk.streamcheckerguitest.persistence.PersistenceManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;

public class StreamListUI extends Application {


    private static final Logger LOGGER = LoggerFactory.getLogger(AddChannelForm
            .class);;
    private static TrayIcon trayIcon;
    private static Stage primaryStage;
    public static BalloonTipManager ballonManager;


    public static void startMainWindow() {
        launch();
    }



    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        StreamListUI.primaryStage = primaryStage;

        //javax.swing.SwingUtilities.invokeLater(this::createTrayIcon);

        createTrayIcon();

        FXMLLoader fxmlLoader = new FXMLLoader();
        URL location = Main.class.getClass().getResource("/fxml/sample.fxml");
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        Parent root = fxmlLoader.load(location.openStream());
        primaryStage.initStyle(StageStyle.UNDECORATED);

        primaryStage.getIcons().add(new javafx.scene.image.Image(Main.class.getResourceAsStream("/pictures/quader.png")));
        primaryStage.setTitle("XDDDDDDDDDDDDDDDDDDDDDDD");
        Scene scene = new Scene(root, 300, 700);
        String css = this.getClass().getResource("/test.css").toExternalForm();
        scene.getStylesheets().add(css);
        primaryStage.setScene(scene);
        Platform.setImplicitExit(false);
        primaryStage.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                LOGGER.trace("Focus event " + observable + " " + oldValue + " " + newValue);
                if (!newValue) {
                    //primaryStage.hide();
                }
            }
        });
    }

    public void createTrayIcon() {
        try {
            java.awt.Toolkit.getDefaultToolkit();
            // app requires system tray support, just exit if there is no support.
            if (!java.awt.SystemTray.isSupported()) {
                LOGGER.error("No system tray support, application exiting.");
                Platform.exit();
            }

            SystemTray tray = SystemTray.getSystemTray();
            Image icon = ImageIO.read(StreamListUI.class.getResourceAsStream("/pictures/quader.png"));
            trayIcon = new TrayIcon(icon);
            trayIcon.setImageAutoSize(true);
            trayIcon.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // TODO Auto-generated method stub
                    super.mouseClicked(e);
                    LOGGER.debug("dddd");

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            LOGGER.debug("Gonna show StreamListUI, in thread");
                            Platform.runLater(StreamListUI.this::showStage);
                        }
                    });
                }
            });

            LOGGER.debug("Add TrayIcon to tray");
            tray.add(trayIcon);
            ballonManager = new BalloonTipManager(trayIcon);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Platform.runLater(StreamListUI.this::showStage);

    }

    public void showStage() {
        Dimension scrnSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle winSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        Point mouse = MouseInfo.getPointerInfo().getLocation();

        int taskBarHeight = scrnSize.height - winSize.height + 10;
        int taskBarWidth = scrnSize.width - winSize.width + 10;
        LOGGER.debug("TaskbarHeight: " + taskBarHeight);
        LOGGER.debug("TaskBarWidth: " + taskBarWidth);

        double dektopheight = Toolkit.getDefaultToolkit().getScreenSize()
                .getHeight();
        double desktopwidth = Toolkit.getDefaultToolkit().getScreenSize()
                .getWidth();

        LOGGER.debug("Desktop Height: " + dektopheight);
        LOGGER.debug("Desktop Width: " + desktopwidth);

        double winWidth = winSize.getWidth();
        LOGGER.debug("WinWidth: " + winWidth);


        int locationX = (int) MouseInfo.getPointerInfo().getLocation().getX();
        if (locationX + primaryStage.getWidth() > desktopwidth)
            locationX = (int) (desktopwidth - primaryStage.getWidth());



        MouseLocation mouseLocation = getMouseLocation(scrnSize);

        LOGGER.debug("MouseLocation: " + mouseLocation);

        TaskbarStare state = getTaskbarLocation(scrnSize, winSize);

        LOGGER.debug("TaskbarState: " + state);

        double positionX = 0;
        double positionY = 0;

        if (mouseLocation == MouseLocation.DownLeft && state == TaskbarStare.Vertical) {
            positionX = desktopwidth - winWidth;
            positionY = mouse.getY() - primaryStage.getHeight();

        } else if (mouseLocation == MouseLocation.DownRight && state == TaskbarStare.Horizontal) {
            positionX = mouse.getX();
            positionY = dektopheight - taskBarHeight - primaryStage.getHeight();
        } else if (mouseLocation == MouseLocation.DownRight && state ==
                TaskbarStare.Vertical) {
            positionX = desktopwidth - primaryStage.getWidth() - taskBarWidth;
            positionY = mouse.getY() - primaryStage.getHeight();
        } else if (mouseLocation == MouseLocation.UpperRight && state ==
                TaskbarStare.Horizontal) {
            positionX = mouse.getX() - primaryStage.getWidth();
            positionY = taskBarHeight;
        }


        primaryStage.setY(positionY);
        primaryStage.setX(positionX);

        //primaryStage.setY(MouseInfo.getPointerInfo().getLocation().getY());
        //primaryStage.setX(MouseInfo.getPointerInfo().getLocation().getX());
        outOfBoundsCheck(scrnSize);

        LOGGER.debug("Window displayed at X: " + primaryStage.getX() + " Y: "
                + primaryStage.getY());

        primaryStage.show();

    }


    private MouseLocation getMouseLocation(Dimension screenSize) {
        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        double middleX = screenSize.getWidth() / 2.0;
        double middleY = screenSize.getHeight() / 2.0;

        if (middleX < mouseLocation.getX() && middleY < mouseLocation.getY())
            return MouseLocation.DownRight;
        else if (middleX < mouseLocation.getX() && middleY > mouseLocation
                .getY())
            return MouseLocation.UpperRight;
        else if (middleX > mouseLocation.getY() && middleY > mouseLocation.getY())
            return MouseLocation.UpperLeft;
        else
            return MouseLocation.DownLeft;
    }

    private TaskbarStare getTaskbarLocation(Dimension screenSize,
                                            Rectangle windowSize) {

        try {
            if(PersistedSettingsManager.getInstance().getOs() == PersistenceManager.OperatingSystem.Windows)
            {
                if (screenSize.getWidth() != windowSize.getWidth())
                    return TaskbarStare.Vertical;
                else
                    return TaskbarStare.Horizontal;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return TaskbarStare.Horizontal;

    }

    private void outOfBoundsCheck(Dimension screensize)
    {
        if(primaryStage.getY() + primaryStage.getHeight() > screensize.getHeight())
            primaryStage.setY(screensize.getHeight() - primaryStage.getHeight());

        if(primaryStage.getX() + primaryStage.getWidth() > screensize.getWidth())
            primaryStage.setX(screensize.getWidth() - primaryStage.getWidth());
    }

    private enum MouseLocation {
        DownLeft,
        DownRight,
        UpperLeft,
        UpperRight
    }

    private enum TaskbarStare {
        Vertical,
        Horizontal
    }


}
