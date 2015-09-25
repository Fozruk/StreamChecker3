package com.github.fozruk.streamcheckerguitest.vlcgui.ui;

import javax.swing.*;
import javax.swing.ListCellRenderer;
import java.awt.*;

/**
 * Created by Philipp on 18.08.2015.
 *
 */
public class ResizeableList extends JList implements Scrollable {

    public ResizeableList(DefaultListModel model)
    {
        super(model);
        setFixedCellWidth(-1);
        setBackground(new Color(23, 23, 23));
    }

    public ResizeableList(DefaultListModel model, ListCellRenderer cellRenderer)
    {
        this(model);
        setCellRenderer(cellRenderer);
    }

    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 10;
    }

    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return ((orientation == SwingConstants.VERTICAL) ? visibleRect.height : visibleRect.width) - 10;
    }

    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    public boolean getScrollableTracksViewportHeight() {
        return false;
    }

    @Override
    public void updateUI() {
        super.updateUI();
    }


}
