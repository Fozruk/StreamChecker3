package com.github.fozruk.streamcheckerguitest.chat.twitch;

import com.github.epilepticz.streamchecker.exception.ReadingWebsiteFailedException;
import com.github.epilepticz.streamchecker.model.channel.interf.IChannel;
import com.github.epilepticz.streamchecker.util.WebUtils;
import com.github.fozruk.streamcheckerguitest.chat.ChatObserver;
import com.github.fozruk.streamcheckerguitest.chat.IChat;
import com.github.fozruk.streamcheckerguitest.persistence.PersistedSettingsManager;
import com.github.fozruk.streamcheckerguitest.vlcgui.ui.ChatMessage;
import com.github.fozruk.streamcheckerguitest.util.Util;
import com.google.common.collect.ImmutableMap;
import org.pircbotx.PircBotX;
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
import java.util.StringTokenizer;

/**
 * Created by Philipp on 12.08.2015.
 */
public class TwitchImplNew extends ListenerAdapter implements IChat
{

    private String token;
    private String username;
    private PircBotXTwitch bot;
    IChannel channel;
    private ChatObserver observer;
    private static PircBotXTwitch whisperServer;
    private static final Logger LOGGER = LoggerFactory.getLogger(TwitchImplNew.class);
    private static ArrayList<TwitchImplNew> whisperObserver = new ArrayList<>();

    private static final String TWITCH_IRC_GROUP_SERVER= "http://tmi.twitch" +
            ".tv/servers?cluster=group";
    private String tempmessage;

    public TwitchImplNew(IChannel channel) throws
            IOException,
            IrcException,
            ReadingWebsiteFailedException, JSONException {

        this.username = (PersistedSettingsManager.getInstance().getValue
                ("TwitchTV.username"));

        this.token = PersistedSettingsManager.getInstance()
                .getValue("TwitchTV.token");

        this.channel = channel;
        startWhisperServerIfNeeded();
    }

    //Hier kommen IRC Messages an
    @Override
    public void onUnknown(UnknownEvent event) throws Exception {
        super.onUnknown(event);
        if(event.getLine().contains("USERSTATE")) {
            ChatMessage msg = new ChatMessage(tempmessage);
            msg.setUsername("Fozruk");
            observer._onMessage(msg);
        }
        else if(event.getLine().contains("WHISPER"))
        {
            ChatMessage msg = new ChatMessage(event.getLine());
            msg.setUsername( "WHIPSER -> " + this.getUsername());
            observer._onMessage(msg);
        } else {
            observer._onMessage(new ChatMessage(event.getLine()));
        }

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

    @Override
    public void onDisconnect(DisconnectEvent event) throws Exception {
        super.onDisconnect(event);
            observer._onMessage(new ChatMessage("Server", "Disconnected"));
    }

    @Override
    public void _sendMessage(String channelname, String message) {
        if(message.startsWith("/w"))
        {
            whisperServer.sendRaw().rawLine("PRIVMSG #"+channelname+" :"+message);
        }else
        {
            this.tempmessage = message;
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
    public void onConnect(ConnectEvent event) throws Exception {
        super.onConnect(event);
        event.getBot().sendRaw().rawLine("CAP REQ :twitch.tv/tags");
        event.getBot().sendRaw().rawLine("CAP REQ :twitch" +
                ".tv/commands");

        event.getBot().sendRaw().rawLine("JOIN #" + channel.getChannelName()
                .toLowerCase());
    }

    @Override
    public void disconnect() {
        //bot.stopBotReconnect();
        //bot.shutdown();
        LOGGER.info("Gonna quit from the Server, bye!");
        bot.sendIRC().quitServer();
        whisperObserver.remove(this);
        LOGGER.debug("Observers for Whisper: " + whisperObserver.size());
    }

    //TODO in andere Klasse auslagern
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

    /**
     * Starts the Bot
     * @throws IOException
     * @throws ReadingWebsiteFailedException
     * @throws JSONException
     */
    @Override
    public void start() throws IOException, ReadingWebsiteFailedException, JSONException {
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
                .setServerPassword(token).buildConfiguration();
        bot = new PircBotXTwitch(conf,channel.getChannelName());
        bot.start();
    }

    public void setObserver(ChatObserver observer)
    {
        this.observer = observer;
    }

    private void startWhisperServerIfNeeded() throws ReadingWebsiteFailedException, JSONException, IOException {
        if(whisperServer == null)
        {
            LOGGER.info("Whisper Server NULL, gonna connect...");
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
                            .getValue("TwitchTV.token")).buildConfiguration();
            whisperServer = new PircBotXTwitch(conf,"WhisperServer");
            whisperServer.start();
        }
    }
}
