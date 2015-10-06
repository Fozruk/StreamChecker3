package com.github.fozruk.streamcheckerguitest.vlcgui.ui;

import com.github.fozruk.streamcheckerguitest.vlcgui.controller.VlcLivestreamController;

import javax.swing.*;
import javax.swing.text.View;
import java.awt.*;

/**
 * Created by Philipp on 12.08.2015.
 */
public class ListCellRenderer extends JLabel implements javax.swing.ListCellRenderer {

    private JList list;

    public ListCellRenderer()
    {
        System.out.println("Allocated");
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        ChatMessage person = (ChatMessage) value;

        this.list = list;
        setOpaque(true);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(0);
        setAlignmentY(0);

         setText("<html><body><font " +
                 "color=\"#8c8c8c\">"+person.getTime()+"</font><font-" +
                 " " +
                 "family:'Helvetica Neue', Helvetica, Arial, " +
                 "sans-serif><font " +
                 "color="+person
                 .getColor()+">" +
                 person.getUsername() +
                 "</font><br><font color=#e7e7e7>" + person.getMessage() +
                 "</font></body></html>");

        this.setPreferredSize(getPreferredSize());



        if(index % 2 == 0)
        {
            setBackground(new Color(23, 23, 23));
            setForeground(new Color(150, 150, 150));
        } else
        {
            setBackground(new Color(0x1E, 0x1E, 0x1E));
            setForeground(new Color(150, 150, 150));
        }


        if(VlcLivestreamController.highligter.shallBeHighlighted(person.getMessage()))
        {
            setBackground(Color.green.darker().darker());
        }

        if(VlcLivestreamController.highligter.shallBeHighlighted(person.getUsername()))
        {
            setBackground(Color.green.darker().darker());
        }

        return this;
    }

    public java.awt.Dimension getPreferredSize() {

        return new Dimension(list.getWidth(),getPreferredHeight(this,true,
                list.getWidth()).height);
    }

    //
    public static java.awt.Dimension getPreferredHeight(JLabel label,
                                                      boolean width, int prefSize) {


        View view = (View) label.getClientProperty(javax.swing.plaf.basic
                .BasicHTML.propertyKey);

        view.setSize(width ? prefSize : 0, width ? 0 : prefSize);

        float w = view.getPreferredSpan(View.X_AXIS);
        float h = view.getPreferredSpan(View.Y_AXIS);

        return new java.awt.Dimension((int) Math.ceil(w), (int) Math.ceil(h));
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("xd");
    }

}