package com.github.fozruk.streamcheckerguitest.vlcgui.ui;

import com.github.epilepticz.streamchecker.exception.ReadingWebsiteFailedException;
import com.github.epilepticz.streamchecker.model.channel.interf.IChannel;
import com.github.epilepticz.streamchecker.model.channel.interf.IChannelobserver;
import com.github.fozruk.streamcheckerguitest.chat.ChatBuffer;
import com.github.fozruk.streamcheckerguitest.persistence.PersistedSettingsManager;
import com.github.fozruk.streamcheckerguitest.persistence.PersistenceManager;
import com.github.fozruk.streamcheckerguitest.vlcgui.controller.VlcLivestreamController;
import com.github.fozruk.streamcheckerguitest.vlcgui.vlcj.sampleCanvas;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;

public class StreamWindow extends JFrame implements IChannelobserver {

    private static final Logger LOGGER = LoggerFactory.getLogger(StreamWindow.class);
    private final JScrollPane viewerListPane;
    private final ViewerList viewerList;

    private JSlider slider = new JSlider(0, 200);

    private JSplitPane splitPane;

    //VLC
    private sampleCanvas vlcPlayerCanvas;

    //ResizeableList
    private ChatWindow chatWindow;
    private JScrollPane chatWindowScrollPane;
    private DefaultListModel<ChatMessage> chatListModel;
    private JTextField textField;
    private JCheckBox toggleAutoscrollBox;
    private DefaultListModel<String> viewerlist;
    private JSpinner chatSpeed;
    private ChatBuffer chatBuffer;
    private final Font FONT = new JLabel().getFont();


    //Colors for Look and Feel
    static Color myColor = Color.darkGray.darker().darker();
    static Color fontColor = Color.lightGray;

    //Pictures for Look and Feel
    private static BufferedImage BUTTON_NORMAL = null;
    private static BufferedImage BUTTON_HOVER = null;
    private static BufferedImage BUTTON_PRESSED = null;

    //Controller
    VlcLivestreamController controller;

    private boolean isInFullscreen;

    AdjustmentListener listener = ((e) -> e.getAdjustable().setValue(e.getAdjustable().getMaximum()));

    static
    {
        try {
            BUTTON_NORMAL = ImageIO.read(VlcLivestreamController.class.getResourceAsStream
                    ("/pics/ButtonNormal.png"));
            BUTTON_HOVER = ImageIO.read(VlcLivestreamController.class.getResourceAsStream
                ("/pics/ButtonMouseover.png"));
            BUTTON_PRESSED = ImageIO.read(VlcLivestreamController.class.getResourceAsStream
                ("/pics/ButtonPressed.png"));


            LookAndFeel(Color.black);
//            try {
////                setUIFont (new javax.swing.plaf.FontUIResource(new Font("Lucida Grande",Font.PLAIN, 9)));
////                String st = UIManager.getSystemLookAndFeelClassName();
////                LOGGER.debug("Look and Feel String name: " + st);
////
////                if(st.equals("javax.swing.plaf.metal.MetalLookAndFeel"))
////                {
////                    LOGGER.warn("Metal Look and Feel detected, try to apply GTK Theme");
////                    st = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
////                }
////                UIManager.setLookAndFeel(st);
////                UIManager.put("Slider.paintValue", false);
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            } catch (InstantiationException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (UnsupportedLookAndFeelException e) {
//                e.printStackTrace();
//            }



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final JTabbedPane tabbedPane;
    private Point savedLocation;
    private Rectangle savedBounds;
    private Dimension savedSize;

    /**
     * Create the frame.
     */
    public StreamWindow(VlcLivestreamController controller) throws IOException {


        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                onWindowClosed();
            }
        });
        this.controller = controller;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 1081, 629);
        slider.setPreferredSize(new Dimension(100, 26));
        slider.setMaximumSize(new Dimension(70, 26));
        slider.addChangeListener((e) -> controller.setVolume(slider.getValue()));
        slider.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (e.getKeyChar() == '+')
                {
                    slider.setValue(slider.getValue() + 10);
                }
            }
        });

        JPanel basePane = new JPanel();
        basePane.setBorder(new EmptyBorder(5, 5, 5, 5));
        basePane.setLayout(new BorderLayout(0, 0));
        setContentPane(basePane);





        splitPane = new JSplitPane();
        basePane.add(splitPane, BorderLayout.CENTER);
        basePane.setBorder(null);
        splitPane.setBorder(null);


        vlcPlayerCanvas = new sampleCanvas();

        vlcPlayerCanvas.setBackground(Color.black);
        splitPane.setLeftComponent(vlcPlayerCanvas);

        splitPane.getLeftComponent().setMinimumSize(new Dimension(getWidth()
                - 300, 100));

        chatListModel = new DefaultListModel<ChatMessage>();

        JPanel panel_1 = new JPanel();
        panel_1.setAlignmentY(Component.TOP_ALIGNMENT);
        panel_1.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel_1.setLayout(new BorderLayout(0, 0));

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        panel_1.add(tabbedPane, BorderLayout.CENTER);

        chatWindowScrollPane = new JScrollPane();

        tabbedPane.addTab("ResizeableList", null, chatWindowScrollPane, null);
        this.viewerlist = new DefaultListModel<>();
        viewerList = new ViewerList(viewerlist);

        viewerListPane = new JScrollPane(viewerList);

        tabbedPane.addTab("Viewer", null, viewerListPane, null);

        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                //If index == 1 reload viewerlist
                if (tabbedPane.getSelectedIndex() == 1) {
                    try {
                        StreamWindow.this.viewerlist.removeAllElements();
                        String[] viewers = new String[0];
                        viewers = controller.reloadViewerList();
                        for (String temp : viewers)
                            StreamWindow.this.viewerlist.addElement(temp);

                    } catch (ReadingWebsiteFailedException e1) {
                        e1.printStackTrace();
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    } catch (MalformedURLException e1) {
                        e1.printStackTrace();
                    }

                }
            }
        });

        chatWindowScrollPane.setMaximumSize(new Dimension(300, 10000));
        chatWindowScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        viewerListPane.setMaximumSize(new Dimension(300, 10000));
        viewerListPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        ListCellRenderer cellRenderChat = new ListCellRenderer();

        chatWindow = new ChatWindow(chatListModel,cellRenderChat);

        ComponentListener l = new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                chatWindow.updateUI();
            }
        };

        chatWindow.addComponentListener(l);
        chatWindow.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        chatWindowScrollPane.setViewportView(chatWindow);


        viewerListPane.setViewportView(viewerList);

        splitPane.setRightComponent(panel_1);

        JPanel sendMessagesPane = new JPanel();
        sendMessagesPane.setMaximumSize(new Dimension(32767, 50));
        panel_1.add(sendMessagesPane, BorderLayout.SOUTH);
        sendMessagesPane.setLayout(new BoxLayout(sendMessagesPane, BoxLayout.Y_AXIS));

        JPanel firstPane = new JPanel();
        JPanel secondPane = new JPanel();

        sendMessagesPane.add(firstPane);
        sendMessagesPane.add(secondPane);
        firstPane.setLayout(new BoxLayout(firstPane, BoxLayout.X_AXIS));
        secondPane.setLayout(new BoxLayout(secondPane,BoxLayout.X_AXIS));

        textField = new JTextField();
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (e.getKeyCode() == 10) {
                    controller.sendMessage(textField.getText());
                    textField.setText("");
                }
            }
        });
        firstPane.add(textField);
        textField.setColumns(10);


        JButton sendMessageButton = new JButton("Send");
        sendMessageButton.setVerticalAlignment(SwingConstants.TOP);
        sendMessageButton.setHorizontalAlignment(SwingConstants.LEADING);
        sendMessageButton.addActionListener((e) -> {
            controller.sendMessage(textField.getText());

            ChatMessage person = new ChatMessage(controller
                    .getUsername(),
                    textField.getText());

            textField.setText("");

            appendChatMessage(person);
        });
        firstPane.add(sendMessageButton);

        toggleAutoscrollBox = new JCheckBox("");
        toggleAutoscrollBox.setActionCommand("");
        toggleAutoscrollBox.setSelected(true);
        toggleAutoscrollBox.addActionListener((e) -> {
            if (toggleAutoscrollBox.isSelected()) {
                addAutoScroll();
            } else {
                removeAutoScroll();
            }
        });
        secondPane.add(toggleAutoscrollBox);
        secondPane.add(slider);
        setTitle("Loading Channel Data....");
        addAutoScroll();

        vlcPlayerCanvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                LOGGER.debug("Fullscreen Mouse event");
                controller.toggleFullscreen();
            }
        });

        splitPane.setResizeWeight(0.9);


        this.setIconImage(ImageIO.read(getClass().getResource("/pictures/IconJframe.png")));

        chatBuffer = new ChatBuffer(chatListModel,chatWindow);


        chatSpeed = new JSpinner(new SpinnerNumberModel(250,1,2000,1));
        chatSpeed.setName("Buffer");
        secondPane.add(chatSpeed);

        JButton restart = new JButton("Restart");
        Runnable restartLivestreamer = () -> {
            try {
                controller.restartLivestreamer();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ReadingWebsiteFailedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        restart.addActionListener((e) -> new Thread(restartLivestreamer).start());
        secondPane.add(restart);
        chatSpeed.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                chatBuffer.setDelay((Integer) chatSpeed.getValue());
            }
        });

        SwingUtilities.invokeLater(()->{
            setVisible(true);
            toFront();
        });

    }

    //Invoked if the window gets closed
    private void onWindowClosed() {
        LOGGER.info("Window closed");
        controller.stopWindow();
    }

    public synchronized void appendChatMessage(ChatMessage message) {
        SwingUtilities.invokeLater(() -> chatBuffer.addMessage(message));
        //SwingUtilities.invokeLater(() -> chatListModel.addElement(message));
    }

    public sampleCanvas getVlcPlayerCanvas() {
        return vlcPlayerCanvas;
    }

    private void addAutoScroll() {
        chatWindowScrollPane.getVerticalScrollBar().addAdjustmentListener(listener);
    }

    private void removeAutoScroll() {
        chatWindowScrollPane.getVerticalScrollBar().removeAdjustmentListener(listener);
    }

    @Override
    public void recieveNotification(IChannel sender, boolean isOnline) {

    }

    @Override
    public void receiveStatusString(String message) {
        setTitle(message);
    }

    //Design
    //UI Design Stuffs

    public static void LookAndFeel(Color c) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());

                    UIManager.getLookAndFeelDefaults().put("ComboBox:\"ComboBox.listRenderer\".background", new javax.swing.plaf.ColorUIResource(myColor));
                    UIManager.getLookAndFeelDefaults().put("ComboBox:\"ComboBox.cellRenderChat\"[Disabled].textForeground", new javax.swing.plaf.ColorUIResource(myColor));
                    UIManager.getLookAndFeelDefaults().put("ComboBox:\"ComboBox.listRenderer\"[Selected].background", new javax.swing.plaf.ColorUIResource(c));
                    UIManager.getLookAndFeelDefaults().put("ComboBox:\"ComboBox.cellRenderChat\"[Selected].background", new javax.swing.plaf.ColorUIResource(c));

                    UIManager.getLookAndFeelDefaults().put("ComboBox[Editable+Focused].backgroundPainter",
                            new MenubarPainter(c, c.brighter()));
                    UIManager.getLookAndFeelDefaults().put("ComboBox.disabledBackground", new javax.swing.plaf.ColorUIResource(Color.BLACK));
                    UIManager.put("TextField.background", myColor);
                    UIManager.put("TextField.foreground", Color.LIGHT_GRAY);

                    UIManager.put("ToolBar.background", c);
                    UIManager.getLookAndFeelDefaults().put("FileChooser.listViewBackground", myColor);

                    UIManager.put("Viewport.background", myColor);
                    UIManager.put("Viewport.foreground", Color.LIGHT_GRAY);


                    UIManager.getLookAndFeelDefaults().put("FileChooser.background", myColor);
                    UIManager.getLookAndFeelDefaults().put("List.Background", myColor);
                    UIManager.getLookAndFeelDefaults().put("FileChooser[Enabled].backgroundPainter",
                            new MenubarPainter(myColor.brighter(), myColor));
                    UIManager.getLookAndFeelDefaults().put("MenuBar[Enabled].backgroundPainter",
                            new MenubarPainter(new Color(127, 255, 191)));

                    UIManager.getLookAndFeelDefaults().put("FileChooser.background", new javax.swing.plaf.ColorUIResource(myColor));
                    UIManager.getLookAndFeelDefaults().put("nimbusLightBackground", new javax.swing.plaf.ColorUIResource(myColor));
                    UIManager.getLookAndFeelDefaults().put("background", new javax.swing.plaf.ColorUIResource(myColor));
                    UIManager.getLookAndFeelDefaults().put("menuText", new javax.swing.plaf.ColorUIResource(Color.LIGHT_GRAY));
                    UIManager.getLookAndFeelDefaults().put("textForeground", new javax.swing.plaf.ColorUIResource(fontColor));
                    UIManager.getLookAndFeelDefaults().put("Menu.background", new javax.swing.plaf.ColorUIResource(myColor));
                    UIManager.getLookAndFeelDefaults().put("MenuBar.background", new javax.swing.plaf.ColorUIResource(myColor));
                    UIManager.getLookAndFeelDefaults().put("Button[Enabled].backgroundPainter",
                            new ImagePainter(new Color(127, 255, 191), BUTTON_NORMAL));

                    UIManager.getLookAndFeelDefaults().put("Button[Disabled].textForeground",
                            new javax.swing.plaf.ColorUIResource(myColor));

                    UIManager.getLookAndFeelDefaults().put("Button[Focused].backgroundPainter",
                            new ImagePainter(new Color(127, 255, 191), BUTTON_NORMAL));

                    UIManager.getLookAndFeelDefaults().put("Button[Focused+MouseOver].backgroundPainter",
                            new ImagePainter(new Color(127, 255, 191), BUTTON_HOVER));

                    UIManager.getLookAndFeelDefaults().put("Button[MouseOver].backgroundPainter",
                            new ImagePainter(new Color(127, 255, 191), BUTTON_HOVER));

                    UIManager.getLookAndFeelDefaults().put("Button[Default+Focused+MouseOver].backgroundPainter",
                            new ImagePainter(new Color(127, 255, 191), BUTTON_HOVER));

                    UIManager.getLookAndFeelDefaults().put("Button[Default+Focused].backgroundPainter",
                            new ImagePainter(new Color(127, 255, 191), BUTTON_NORMAL));

                    UIManager.getLookAndFeelDefaults().put("Button[Default+MouseOver].backgroundPainter",
                            new ImagePainter(new Color(127, 255, 191), BUTTON_HOVER));

                    UIManager.getLookAndFeelDefaults().put("Button[Pressed].backgroundPainter",
                            new ImagePainter(new Color(127, 255, 191), BUTTON_PRESSED));

                    UIManager.getLookAndFeelDefaults().put("Button[Default+Pressed].backgroundPainter",
                            new ImagePainter(new Color(127, 255, 191), BUTTON_PRESSED));

                    UIManager.getLookAndFeelDefaults().put("Button[Default+Focused+Pressed].backgroundPainter",
                            new ImagePainter(new Color(127, 255, 191), BUTTON_PRESSED));


                    UIManager.getLookAndFeelDefaults().put("Button[Focused+Pressed].backgroundPainter",
                            new ImagePainter(new Color(127, 255, 191), BUTTON_PRESSED));

                    UIManager.getLookAndFeelDefaults().put("Button[Disabled].backgroundPainter",
                            new ImagePainter(new Color(127, 255, 191), BUTTON_NORMAL));

                    UIManager.getLookAndFeelDefaults().put("Button[Default].backgroundPainter",
                            new ImagePainter(new Color(127, 255, 191), BUTTON_NORMAL));

                    UIManager.getLookAndFeelDefaults().put("ComboBox[Enabled].backgroundPainter",
                            new ImagePainter(new Color(127, 255, 191), BUTTON_NORMAL));

                    UIManager.getLookAndFeelDefaults().put("ComboBox[MouseOver].backgroundPainter",
                            new ImagePainter(new Color(127, 255, 191), BUTTON_HOVER));

                    UIManager.getLookAndFeelDefaults().put("ComboBox[Focused+Pressed].backgroundPainter",
                            new ImagePainter(new Color(127, 255, 191), BUTTON_PRESSED));

                    UIManager.getLookAndFeelDefaults().put("ComboBox[MouseOver].backgroundPainter",
                            new ImagePainter(new Color(127, 255, 191), BUTTON_HOVER));

                    UIManager.getLookAndFeelDefaults().put("ComboBox[Focused+MouseOver].backgroundPainter",
                            new ImagePainter(new Color(127, 255, 191), BUTTON_HOVER));

                    UIManager.getLookAndFeelDefaults().put("ComboBox[Focused].backgroundPainter",
                            new ImagePainter(new Color(127, 255, 191), BUTTON_NORMAL));

                    UIManager.getLookAndFeelDefaults().put("ComboBox.background", Color.RED);

                    UIManager.getLookAndFeelDefaults().put("ComboBox:\"ComboBox.listRenderer\"[Selected].background",
                            new javax.swing.plaf.ColorUIResource(myColor));


                    UIManager.getLookAndFeelDefaults().put("ComboBox[Disabled].backgroundPainter",
                            new ImagePainter(new Color(127, 255, 191), BUTTON_NORMAL));

                    UIManager.getLookAndFeelDefaults().put("ScrollBar:ScrollBarThumb[Enabled].backgroundPainter",
                            new MenubarPainter(new Color(127, 255, 191).brighter().brighter()));

                    UIManager.getLookAndFeelDefaults().put("ScrollBar:ScrollBarThumb[MouseOver].backgroundPainter",
                            new MenubarPainter(new Color(127, 255, 191).brighter()));

                    UIManager.getLookAndFeelDefaults().put("ScrollBar:\"ScrollBar.button\"[Enabled].foregroundPainter",
                            new MenubarPainter(new Color(127, 255, 191).brighter()));

                    UIManager.getLookAndFeelDefaults().put("ProgressBar[Enabled].backgroundPainter",
                            new MenubarPainter(new Color(0, 0, 0), myColor));



                    UIManager.getLookAndFeelDefaults().put("SplitPane.background", myColor);

                    UIManager.getLookAndFeelDefaults().put("SplitPane:SplitPaneDivider[Enabled+Vertical].foregroundPainter",
                            new MenubarPainter(new Color(0, 0, 0), myColor));
                    UIManager.getLookAndFeelDefaults().put("SplitPane:SplitPaneDivider[Enabled].backgroundPainter",
                            new MenubarPainter(new Color(0, 0, 0), myColor));
                    UIManager.getLookAndFeelDefaults().put("SplitPane:SplitPaneDivider[Enabled].foregroundPainter",
                            new MenubarPainter(new Color(0, 0, 0), myColor));
                    UIManager.getLookAndFeelDefaults().put("SplitPane:SplitPaneDivider[Focused].backgroundPainter",
                            new MenubarPainter(new Color(0, 0, 0), myColor));

                    UIManager.getLookAndFeelDefaults().put("ProgressBar[Enabled].foregroundPainter",
                            new MenubarPainter(c, c.darker().darker()));

                    UIManager.getLookAndFeelDefaults().put("ProgressBar[Enabled+Finished].foregroundPainter",
                            new MenubarPainter(c, c.darker().darker()));


                    UIManager.getLookAndFeelDefaults().put("MenuItem[MouseOver].backgroundPainter",
                            new MenubarPainter(c, c.darker().darker()));

                    UIManager.getLookAndFeelDefaults().put("MenuItem.background", new javax.swing.plaf.ColorUIResource(myColor));
                    UIManager.getLookAndFeelDefaults().put("MenuItem.foreground", new javax.swing.plaf.ColorUIResource(fontColor));
                    UIManager.getLookAndFeelDefaults().put("MenuItem[MouseOver].textForeground", new javax.swing.plaf.ColorUIResource(fontColor));

                    UIManager.getLookAndFeelDefaults().put("nimbusBase",
                            new javax.swing.plaf.ColorUIResource(myColor));

                    UIManager.getLookAndFeelDefaults().put("nimbusInfoBlue",
                            new javax.swing.plaf.ColorUIResource(c));

                    UIManager.getLookAndFeelDefaults().put("nimbusSelectionBackground",
                            new javax.swing.plaf.ColorUIResource(c));

                    UIManager.getLookAndFeelDefaults().put("Menu[Enabled+Selected].backgroundPainter",
                            new MenubarPainter(c));

                    UIManager.getLookAndFeelDefaults().put("MenuBar:Menu[Selected].backgroundPainter",
                            new MenubarPainter(c));


                    UIManager.getLookAndFeelDefaults().put("Separator.background",
                            new javax.swing.plaf.ColorUIResource(Color.DARK_GRAY));

                    UIManager.getLookAndFeelDefaults().put("Separator[Enabled].backgroundPainter",
                            new MenubarPainter(myColor.brighter().brighter(), myColor.brighter().brighter()));

                    UIManager.getLookAndFeelDefaults().put("Menu.background", new javax.swing.plaf.ColorUIResource(c));

                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setCanvasToFullscreen() {
        try {
            if(PersistedSettingsManager.getInstance().getOs() != PersistenceManager.OperatingSystem.Windows)
            {
                if(!isInFullscreen)
                {

                    this.savedBounds = (Rectangle) this.getBounds().clone();

                    isInFullscreen = true;
                    this.splitPane.getRightComponent().setVisible(false);
                    this.setAlwaysOnTop(true);
                    getCurrentScreen().setFullScreenWindow(this);
                    makeDividerVisible(false);
                    this.revalidate();
                    getVlcPlayerCanvas().revalidate();
                    this.chatWindow.revalidate();
                } else
                {
                    this.isInFullscreen = false;
                    this.splitPane.getRightComponent().setVisible(true);
                    makeDividerVisible(true);
                    getCurrentScreen().setFullScreenWindow(null);
                    this.setAlwaysOnTop(false);
                    this.setBounds(this.savedBounds);
                    this.revalidate();
                    getVlcPlayerCanvas().revalidate();
                    this.chatWindow.revalidate();
                }
                //this.pack();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void makeDividerVisible(boolean b) {
        Arrays.stream(splitPane.getComponents()).forEach((i) -> {
            if(i instanceof BasicSplitPaneDivider)
            {
                BasicSplitPaneDivider temp = (BasicSplitPaneDivider) i;
                temp.setVisible(b);
            }
        });
    }

    private GraphicsDevice getCurrentScreen()
    {

        GraphicsConfiguration config = this.getGraphicsConfiguration();
        GraphicsDevice myScreen = config.getDevice();

        GraphicsEnvironment ge = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();

        GraphicsDevice myDevice = null;

        for (int i = 0; i < gs.length; i++) {
            if (gs[i].equals(myScreen))
            {
                myDevice = gs[i];
                return myDevice;
            }
        }
        return null;
    }

    private static class MenubarPainter implements Painter<JComponent> {
        private Color colorz = Color.DARK_GRAY;
        private Color colorz2 = myColor;

        public MenubarPainter(Color color) {
        }

        public MenubarPainter(Color color1, Color color2) {
            this.colorz = color1;
            this.colorz2 = color2;
        }

        public void paint(Graphics2D g, JComponent object, int width, int heigth) {
            GradientPaint gp = new GradientPaint(0, 0, this.colorz, 0, 20, colorz2);
            g.setPaint(gp);
            //g.setColor(Color.RED);
            g.fillRect(0, 0, width, heigth);
        }

    }

    private static class ImagePainter implements Painter<JComponent> {
        private BufferedImage path;

        public ImagePainter(Color color, BufferedImage path) {
            this.path = path;

        }

        public void paint(Graphics2D g, JComponent object, int width, int heigth) {
            g.drawImage(path, 0, 0, object.getWidth(), object.getHeight(), null);
        }
    }

    private static void setUIFont(javax.swing.plaf.FontUIResource f)
    {
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements())
        {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource)
            {
                System.out.println(value.toString());
                UIManager.put(key, f);
            }
        }
    }
}
