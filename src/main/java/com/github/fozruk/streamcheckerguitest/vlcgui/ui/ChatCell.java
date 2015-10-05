package com.github.fozruk.streamcheckerguitest.vlcgui.ui;

import com.github.fozruk.streamcheckerguitest.vlcgui.controller.VlcLivestreamController;
import org.pircbotx.dcc.Chat;

import javax.swing.*;
import javax.swing.text.View;
import java.awt.*;

/**
 * Created by Philipp on 04.10.2015.
 */
public class ChatCell extends JLabel {

    public ChatCell()
    {
        setOpaque(true);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(0);
        setAlignmentY(0);

        //this.setPreferredSize(getPreferredSize());

        if(2 % 2 == 0)
        {
            setBackground(new Color(23, 23, 23));
            setForeground(new Color(150, 150, 150));
        } else
        {
            setBackground(new Color(0x1E, 0x1E, 0x1E));
            setForeground(new Color(150, 150, 150));
        }
    }

    public java.awt.Dimension getPreferredSize() {

        return new Dimension(this.getParent().getWidth(),getPreferredHeight(this,true,
                this.getParent().getWidth()).height);
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

    public void setText(String txt)
    {
        super.setText("<html><body>" + txt + "</body></html>");
        if(VlcLivestreamController.highligter.shallBeHighlighted(txt))
        {
            setBackground(Color.green.darker().darker());
        }

        if(VlcLivestreamController.highligter.shallBeHighlighted(txt))
        {
            setBackground(Color.green.darker().darker());
        }
    }
}
