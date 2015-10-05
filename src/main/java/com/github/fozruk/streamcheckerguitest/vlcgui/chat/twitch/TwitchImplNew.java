package com.github.fozruk.streamcheckerguitest.vlcgui.chat.twitch;

import com.github.epilepticz.streamchecker.exception.ReadingWebsiteFailedException;
import com.github.epilepticz.streamchecker.model.channel.interf.IChannel;
import com.github.epilepticz.streamchecker.util.WebUtils;
import com.github.fozruk.streamcheckerguitest.persistence.PersistedSettingsManager;
import com.github.fozruk.streamcheckerguitest.vlcgui.chat.ChatObserver;
import com.github.fozruk.streamcheckerguitest.vlcgui.chat.IChat;
import com.github.fozruk.streamcheckerguitest.vlcgui.ui.ChatMessage;
import org.pircbotx.hooks.events.*;
import org.slf4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pircbotx.Configuration;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.ListenerAdapter;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Philipp on 12.08.2015.
 */
public class TwitchImplNew extends ListenerAdapter implements IChat, ChatObserver {

    private String username;
    private PircBotXTwitch bot;
    IChannel channel;
    private ChatObserver observer;
    Thread botThread;
    private static TwitchImplNew whisperServer;
    private static final Logger LOGGER = LoggerFactory.getLogger(TwitchImplNew.class);
    private static ArrayList<TwitchImplNew> whisperObserver = new ArrayList<>();

    private static final String TWITCH_IRC_GROUP_SERVER= "http://tmi.twitch" +
            ".tv/servers?cluster=group";


    public static void main(String[] args) throws IOException, IrcException, InterruptedException, ReadingWebsiteFailedException, JSONException {

    }

    private TwitchImplNew() {

    }

    public TwitchImplNew(IChannel channel,ChatObserver observer) throws
            IOException,
            IrcException,
            ReadingWebsiteFailedException, JSONException {



        if(whisperServer == null)
        {
            LOGGER.info("Whisper Server NULL, gonna connect...");
            whisperServer = new TwitchImplNew();
            whisperServer.setObserver(this);
        }

        this.channel = channel;
        this.observer = observer;
        this.username = (PersistedSettingsManager.getInstance().getValue
                ("username"));


        String json = WebUtils.readContentFrom(new URL("http://api.twitch" +
                ".tv/api/channels/"+channel.getChannelName()+"/chat_properties"));

        JSONObject jsonObject = new JSONObject(json);

        JSONArray array = jsonObject.getJSONArray("chat_servers");

        String[] serverIp = array.get(0).toString().split(":");



        LOGGER.info("Connecting to: " + serverIp[0] + ":" + serverIp[1]);

        Configuration conf = new Configuration.Builder().setName(username)
                .setServerHostname(serverIp[0]).setEncoding
                (Charset.forName("UTF-8"))
                .setServerPort(Integer.parseInt(serverIp[1]))
                .addListener(this)
                .setServerPassword(PersistedSettingsManager.getInstance()
                        .getValue("token")).buildConfiguration();


        botThread = new Thread(new Runnable() {
            @Override
            public void run() {

                bot = new PircBotXTwitch(conf);

                try {
                    bot.startBot();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (IrcException e) {
                    e.printStackTrace();
                }
            }
        });



        botThread.setName("PircBotX-Thread - " + channel.getChannelName());


        botThread.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        whisperObserver.add(this);
        bot.sendRaw().rawLine("CAP REQ :twitch.tv/tags");
        bot.sendRaw().rawLine("CAP REQ :twitch.tv/commands");
        bot.sendRaw().rawLine("JOIN #" + channel.getChannelName().toLowerCase());

    }

    //Hier kommen IRC Messages an
    @Override
    public void onUnknown(UnknownEvent event) throws Exception {
        super.onUnknown(event);
        observer._onMessage(new ChatMessage(event.getLine()));
    }

    

    //Hier kommen User Messages an
    @Override
    public void onMessage(MessageEvent event) throws Exception {
        super.onMessage(event);
        ChatMessage message = new ChatMessage(event.getUser().getNick(),event
                .getTags()
                .get("color"),event.getMessage());
        observer._onMessage(message);

    }

    //    @Override
//    public void onNotice(NoticeEvent event) throws Exception {
//        super.onNotice(event);
//        System.out.println("test");
//    }

    @Override
    public void onDisconnect(DisconnectEvent event) throws Exception {
        super.onDisconnect(event);
            observer._onMessage(new ChatMessage("Server", "Disconnected"));
    }

//    @Override
//    public void onPrivateMessage(PrivateMessageEvent event) throws Exception {
//        super.onPrivateMessage(event);
//        System.out.println(event.getMessage());
//    }

    @Override
    public void _sendMessage(String channelname, String message) {
        if(message.startsWith("/w"))
        {
            whisperServer.bot.sendRaw().rawLine("PRIVMSG #"+channelname+" :"+message);
        }else
        {
            bot.sendRaw().rawLine("PRIVMSG #"+channelname+" :"+message);
        }
    }

    @Override
    public boolean _isConnected() {
        return false;
    }

    @Override
    public String getChannelName() {
        return channel.getChannelName();
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public void disconnect() {
        //bot.stopBotReconnect();
        bot.shutdown();
        whisperObserver.remove(this);
        LOGGER.debug("Observers for Whisper: " + whisperObserver.size());
    }

    @Override
    public String[] getUserList() throws MalformedURLException,
            ReadingWebsiteFailedException, JSONException {

        ArrayList<String> chatter = new ArrayList<String>();

        URL url = new URL("https://tmi.twitch" +
                ".tv/group/user/"+channel.getChannelName().toLowerCase()+"/chatters");
        LOGGER.info("Get userlist from url: "+ url);
        JSONObject json = new JSONObject(WebUtils.readContentFrom(url));

        JSONArray mods = json.getJSONObject("chatters").getJSONArray
                ("moderators");
        JSONArray viewer = json.getJSONObject("chatters").getJSONArray
                ("viewers");

        for(int i = 0; i < mods.length();i++)
        {
            chatter.add("[MOD]"+mods.get(i));
        }

        for(int i = 0; i < viewer.length();i++)
        {
            chatter.add(viewer.get(i).toString());
        }

        String[] stockArr = new String[chatter.size()];
        return chatter.toArray(stockArr);
    }

    @Override
    public void start() throws IOException, ReadingWebsiteFailedException, JSONException {
        this.username = (PersistedSettingsManager.getInstance().getValue
                ("username"));

        String json = WebUtils.readContentFrom(new URL(TWITCH_IRC_GROUP_SERVER));

        JSONObject jsonObject = new JSONObject(json);

        JSONArray array = jsonObject.getJSONArray("servers");

        String[] serverIp = array.get(0).toString().split(":");

        LOGGER.info("Connecting to: " + serverIp[0] + ":" + serverIp[1]);

        Configuration conf = new Configuration.Builder().setName(username)
                .setServerHostname(serverIp[0]).setEncoding
                        (Charset.forName("UTF-8"))
                .setServerPort(Integer.parseInt(serverIp[1]))
                .addListener(this)
                .setServerPassword(PersistedSettingsManager.getInstance()
                        .getValue("token")).buildConfiguration();


        botThread = new Thread(new Runnable() {
            @Override
            public void run() {

                bot = new PircBotXTwitch(conf);

                try {
                    bot.startBot();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (IrcException e) {
                    e.printStackTrace();
                }
            }
        });

        botThread.setName("PircBotX-Thread: Connected to WhisperServer - " +
                serverIp[0]);

        botThread.start();


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Request these Commands to activate additional features like Whisper
        // Messages and extended chat messages
        bot.sendRaw().rawLine("CAP REQ :twitch.tv/tags");
        bot.sendRaw().rawLine("CAP REQ :twitch.tv/commands");
    }


    //Whisperserver Event

    @Override
    public void _onMessage(ChatMessage message) {
        LOGGER.info("Whisper message: " + message.getMessage());
        for(TwitchImplNew temp : whisperObserver)
            temp.observer._onMessage(message);
    }

    @Override
    public void _onDisconnect() {
        LOGGER.info("Whisper server disconnected");
    }

    public void setObserver(ChatObserver observer)
    {
        this.observer = observer;
    }
}
