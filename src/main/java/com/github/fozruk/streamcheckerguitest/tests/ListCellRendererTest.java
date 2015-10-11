package com.github.fozruk.streamcheckerguitest.tests;

import com.github.epilepticz.streamchecker.exception.CreateChannelException;
import com.github.epilepticz.streamchecker.exception.ReadingWebsiteFailedException;
import com.github.epilepticz.streamchecker.model.channel.impl.MockChannel;
import com.github.epilepticz.streamchecker.model.channel.impl.TwitchTVChannel;
import com.github.epilepticz.streamchecker.model.channel.interf.IChannel;
import com.github.fozruk.streamcheckerguitest.chat.test.NonsenseGenerator;
import com.github.fozruk.streamcheckerguitest.exception.PropertyKeyNotFoundException;
import com.github.fozruk.streamcheckerguitest.vlcgui.controller.VlcLivestreamController;
import com.github.fozruk.streamcheckerguitest.vlcgui.ui.*;
import com.github.fozruk.streamcheckerguitest.vlcgui.ui.ListCellRenderer;
import junit.framework.TestCase;
import org.json.JSONException;
import org.junit.Test;
import org.pircbotx.exception.IrcException;

import javax.swing.*;
import java.io.IOException;
import java.text.NumberFormat;

/**
 * Created by Philipp on 04.10.2015.
 */
public class ListCellRendererTest extends TestCase {



    @Test
    public void teststuff() throws ReadingWebsiteFailedException, IOException, JSONException, IrcException, CreateChannelException, PropertyKeyNotFoundException, InterruptedException {

        IChannel mockChannel = null;
        //try {

        printmem();
            VlcLivestreamController controller = new VlcLivestreamController
                    (new MockChannel());
        // StreamWindow window = new StreamWindow(controller);

       // controller.getStreamWindow().getChatWindow().setCellRenderer(null);



        Thread.sleep(10000000);

        printmem();

//        printmem();
//            mockChannel = new TestChannel();
//            VlcLivestreamController mockController = new VlcLivestreamController
//                    (mockChannel);
//            //StreamWindow mockWindow = new StreamWindow(mockController);
//
//            mockController.getStreamWindow().getChatWindow().setCellRenderer(null);
//            for(int i = 0; i<100000;i++)
//            {
//                mockController._onMessage(new ChatMessage("test"));
//                ((DefaultListModel) mockController.getStreamWindow()
//                        .getChatWindow
//                                ().getModel()).removeElementAt(0);
//            }
//            mockController._onMessage(new ChatMessage("finished"));
//            Thread.sleep(5000);
//            printmem();
//            mockController.getStreamWindow().dispose();
//            int size = ((DefaultListModel) mockController.getStreamWindow()
//                    .getChatWindow
//                    ().getModel()).getSize();
//            System.out.println(size);
//            mockController = null;
//
//
//            Thread.sleep(5000);
//            printmem();







//        } catch (PropertyKeyNotFoundException e) {
//            e.printStackTrace();
//        } catch (IrcException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//

    }

    void printmem()
    {
        Runtime runtime = Runtime.getRuntime();
        StringBuilder sb = new StringBuilder();
        NumberFormat format = NumberFormat.getInstance();
        long maxMemory = runtime.maxMemory();
        long allocatedMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        sb.append("free memory: " + format.format(freeMemory / 1024) + "<br/>");
        sb.append("allocated memory: " + format.format(allocatedMemory / 1024) + "<br/>");
        sb.append("max memory: " + format.format(maxMemory / 1024) + "<br/>");
        sb.append("total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024) + "<br/>");
        System.out.println(sb.toString());
    }


}