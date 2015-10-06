package com.github.fozruk.streamcheckerguitest.util;

import javax.swing.*;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by Philipp on 06.10.2015.
 */
public class Util {

    public static void printExceptionToMessageDialog(Exception e) {
        StringWriter writer = new StringWriter();
        PrintWriter r = new PrintWriter(writer);
        e.printStackTrace(r);
        JOptionPane.showMessageDialog(null, writer.toString(), "ERROR", 0);
    }
}
