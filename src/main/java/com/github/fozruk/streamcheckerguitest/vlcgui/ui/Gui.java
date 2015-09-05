package com.github.fozruk.streamcheckerguitest.vlcgui.ui;

import com.github.epilepticz.streamchecker.model.channel.interf.IChannel;
import com.github.epilepticz.streamchecker.model.channel.interf.IChannelobserver;
import com.github.fozruk.streamcheckerguitest.vlcgui.controller.VlcLivestreamController;
import com.github.fozruk.streamcheckerguitest.vlcgui.vlcj.sampleCanvas;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Gui extends JFrame implements IChannelobserver {

    private ListDataListener listdataListener;
    AdjustmentListener listener = (new AdjustmentListener() {
        public void adjustmentValueChanged(AdjustmentEvent e) {
            e.getAdjustable().setValue(e.getAdjustable().getMaximum());
        }
    });
    private JPanel contentPane;
    private sampleCanvas canvas;
    private Chat list;
    private DefaultListModel model;
    private JSplitPane splitPane;
    private java.util.List<ChatMessage> chatmessages = new ArrayList<>();
    private JScrollPane scrollpane;
    private JPanel panel;
    private JTextField textField;
    private JButton btnNewButton;
    private JCheckBox chckbxNewCheckBox;
    private JPanel panel_1;
    private VlcLivestreamController controller;
    private JSlider slider = new JSlider(0, 200);
    private JTabbedPane tabbedPane;
    private JPanel panel_2;
    private JLabel ircMap;
    private static final Logger LOGGER = LoggerFactory.getLogger(Gui.class);
    private MessageAdder messageAdder;
    private ListCellRenderer renderer;
    static Color myColor = Color.darkGray.darker().darker();
    static Color fontColor = Color.lightGray;
    private static BufferedImage BUTTON_NORMAL = null;
    private static BufferedImage BUTTON_HOVER = null;
    private static BufferedImage BUTTON_PRESSED = null;

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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the frame.
     */
    public Gui(VlcLivestreamController controller) throws IOException {

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

        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                controller.setVolume(slider.getValue());
            }
        });


        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        splitPane = new JSplitPane();
        contentPane.add(splitPane, BorderLayout.CENTER);

        canvas = new sampleCanvas();

        canvas.setBackground(Color.black);
        splitPane.setLeftComponent(canvas);

        splitPane.getLeftComponent().setMinimumSize(new Dimension(getWidth()
                - 300, 100));


        model = new DefaultListModel();

        listdataListener = new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
                if (model.size() > 100)
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            model.remove(0);
                        }
                    });

            }

            @Override
            public void intervalRemoved(ListDataEvent e) {

            }

            @Override
            public void contentsChanged(ListDataEvent e) {

            }
        };

        model.addListDataListener(listdataListener);

        panel_1 = new JPanel();
        panel_1.setAlignmentY(Component.TOP_ALIGNMENT);
        panel_1.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel_1.setLayout(new BorderLayout(0, 0));

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        panel_1.add(tabbedPane, BorderLayout.CENTER);

        scrollpane = new JScrollPane();
        tabbedPane.addTab("Chat", null, scrollpane, null);
        scrollpane.setMaximumSize(new Dimension(300, 10000));
        scrollpane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        list = new Chat(model);

        ComponentListener l = new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                list.updateUI();
            }
        };

        list.addComponentListener(l);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        renderer = new com.github.fozruk
                .streamcheckerguitest.vlcgui.ui.ListCellRenderer();
        list.setCellRenderer(renderer);
        list.setFixedCellWidth(300);
        list.setBackground(new Color(23, 23, 23));
        scrollpane.setViewportView(list);

        panel_2 = new JPanel();
        panel_2.setLayout(new BorderLayout(0, 0));

        ircMap = new JLabel("");
        panel_2.add(ircMap, BorderLayout.CENTER);

        splitPane.setRightComponent(panel_1);

        panel = new JPanel();
        panel.setMaximumSize(new Dimension(32767, 50));
        panel_1.add(panel, BorderLayout.SOUTH);
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        textField = new JTextField();
        textField.setBorder(null);

        panel.add(textField);
        textField.setColumns(10);

        btnNewButton = new JButton("Send");
        btnNewButton.setVerticalAlignment(SwingConstants.TOP);
        btnNewButton.setHorizontalAlignment(SwingConstants.LEADING);
        btnNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.sendMessage(textField.getText());

                ChatMessage person = new ChatMessage(controller.chat
                        .getUsername(),
                        textField.getText());

                textField.setText("");

                appendChatMessage(person);

            }
        });
        panel.add(btnNewButton);

        chckbxNewCheckBox = new JCheckBox("");
        chckbxNewCheckBox.setActionCommand("");
        chckbxNewCheckBox.setSelected(true);
        chckbxNewCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (chckbxNewCheckBox.isSelected()) {
                    addAutoScroll();
                } else {
                    removeAutoScroll();
                }
            }
        });
        panel.add(chckbxNewCheckBox);
        panel.add(slider);
        setVisible(true);
        toFront();
        setTitle("Loading Channel Data....");
        addAutoScroll();

        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                controller.toggleFullscreen();
            }
        });

        splitPane.setResizeWeight(0.9);

        this.setIconImage(ImageIO.read(getClass().getResource("/pictures/IconJframe.png")));

        messageAdder = new MessageAdder(model);
    }

    //Invoked if the window gets closed
    private void onWindowClosed() {
        LOGGER.info("Window closed");
        controller.stopWindow();
    }

    public synchronized void appendChatMessage(ChatMessage message) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               messageAdder.addMessage(message);
            }
        });

    }

    public sampleCanvas getCanvas() {
        return canvas;
    }

    public JList getList() {
        return list;
    }

    private void addAutoScroll() {
        scrollpane.getVerticalScrollBar().addAdjustmentListener(listener);
    }

    private void removeAutoScroll() {
        scrollpane.getVerticalScrollBar().removeAdjustmentListener(listener);
    }

    public void setTitle() {
        setTitle("xddd");
    }


    @Override
    public void recieveNotification(IChannel sender, String message) {

    }

    @Override
    public void receiveStatusString(String message) {
        setTitle(message);
    }

    class MessageAdder
    {
        private DefaultListModel model;
        Thread addThread;
        volatile ArrayList<ChatMessage> messages = new ArrayList();
        private boolean run = true;

        private MessageAdder(DefaultListModel model)
        {
            this.model = model;
            this.addThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while(run)
                    {
                        try {

                            Thread.sleep(500);
                            ArrayList<ChatMessage> temp = (ArrayList<ChatMessage>) messages.clone();
                            messages.clear();
                            for(ChatMessage p : temp)
                            {
                                model.addElement(p);
                            }

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });

            addThread.start();
        }

        public void stop()
        {
            this.run = false;
        }


        public synchronized void addMessage(ChatMessage message) {
            messages.add(message);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        model.removeListDataListener(listdataListener);
    }

    //Design

    //UI Design Stuffs

    public static void LookAndFeel(Color c) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {



                    UIManager.setLookAndFeel(info.getClassName());

                    UIManager.getLookAndFeelDefaults().put("ComboBox:\"ComboBox.listRenderer\".background", new javax.swing.plaf.ColorUIResource(myColor));
                    UIManager.getLookAndFeelDefaults().put("ComboBox:\"ComboBox.renderer\"[Disabled].textForeground", new javax.swing.plaf.ColorUIResource(myColor));
                    UIManager.getLookAndFeelDefaults().put("ComboBox:\"ComboBox.listRenderer\"[Selected].background", new javax.swing.plaf.ColorUIResource(c));
                    UIManager.getLookAndFeelDefaults().put("ComboBox:\"ComboBox.renderer\"[Selected].background", new javax.swing.plaf.ColorUIResource(c));

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
        private final Color colorz;
        private BufferedImage path;

        public ImagePainter(Color color, BufferedImage path) {
            this.colorz = color;
            this.path = path;
        }

        public void paint(Graphics2D g, JComponent object, int width, int heigth) {
            g.drawImage(path, 0, 0, object.getWidth(), object.getHeight(), null);
        }
    }

}
