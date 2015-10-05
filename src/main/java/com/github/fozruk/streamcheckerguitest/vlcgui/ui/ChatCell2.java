package com.github.fozruk.streamcheckerguitest.vlcgui.ui;

import com.github.fozruk.streamcheckerguitest.vlcgui.controller.VlcLivestreamController;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Philipp on 04.10.2015.
 */
public class ChatCell2 extends JTextArea {

    public ChatCell2()
    {
        setOpaque(true);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(0);
        setAlignmentY(0);
        setLineWrap(true);
        setWrapStyleWord(true);

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

    @Override
    public Dimension getPreferredSize() {
        setRows(2);
        return super.getPreferredSize();
    }

    public  int getLineCountAsSeen() {
        Font font = this.getFont();
        FontMetrics fontMetrics = this.getFontMetrics(font);
        int fontHeight = fontMetrics.getHeight();
        int lineCount;
        try {
            int height = this.modelToView(this.getDocument().getEndPosition()
                    .getOffset() - 1).y;
            lineCount = height / fontHeight + 1;
        } catch (Exception e) {
            lineCount = 0;
        }
        System.out.println(lineCount);
        return lineCount;
    }



    public void setText(String txt)
    {
        super.setText(txt);
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
