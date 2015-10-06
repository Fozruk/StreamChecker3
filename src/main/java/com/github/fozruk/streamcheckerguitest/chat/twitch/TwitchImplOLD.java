package com.github.fozruk.streamcheckerguitest.chat.twitch;

/**
 * Created by Philipp on 12.08.2015.
 */
public class TwitchImplOLD {
        //extends PircBot implements IChat {

//    private static final Logger LOGGER = LoggerFactory.getLogger(TwitchImplOLD.class);
//    private Map<AbstractChannel,ChatObserver> observers = new HashMap<>();
//    private static TwitchImplOLD instance;
//    private String username;
//
//
//    private TwitchImplOLD() throws IOException {
//        String username = PersistedSettingsManager.getInstance().getValue
//                ("username");
//
//        String token = PersistedSettingsManager.getInstance().getValue
//                ("token_twitch");
//        _connect("chat.twitch.tv", 6667, username,
//                token);
//    }
//
//    public void _connect(String server,int port,String username,String password)
//    {
//        try {
//            this.setName(username);
//            this.setEncoding("UTF-8");
//            this.connect("chat.twitch.tv", port, password);
//            this.sendRawLine("CAP REQ :twitch.tv/commands");
//            this.sendRawLine("CAP REQ :twitch.tv/membership");
//            this.sendRawLine("CAP REQ :twitch.tv/tags");
//            this.username = username;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void _joinChannel(TwitchTVChannel channel,ChatObserver observer)
//    {
//        observers.put(channel, observer);
//        joinChannel("#" + channel.getChannelName().toLowerCase());
//        observer._onMessage(new ChatMessage("IRC " + this.getVersion(),"Connected " +
//                        "to " +
//                        channel.getChannelName().toLowerCase())
//                );
//    }
//
//    public void _sendMessage(String channelname, String message) {
//        LOGGER.info("Send Message to: " + channelname + " Channelname:" +
//                " " + message);
//        this.sendMessage(channelname, message);
//    }
//
//    @Override
//    protected void onPrivateMessage(String sender, String login, String hostname, String message) {
//        super.onPrivateMessage(sender, login, hostname, message);
//        LOGGER.info("Private Message from: " + sender + "Message: " + message);
//
//        onMessage(null, sender, null, null, message);
//    }
//
//    @Override
//    protected void onUnknown(String line) {
//        super.onUnknown(line);
//        LOGGER.info("OnMessage: " + line);
//
//        Iterator it = observers.entrySet().iterator();
//
//        ChatMessage chatMessage = new ChatMessage(line);
//
//        while (it.hasNext()) {
//            Map.Entry pair = (Map.Entry)it.next();
//            TwitchTVChannel channel = (TwitchTVChannel) pair.getKey();
//            if(channel != null && channel.getChannelName()
//                    .toLowerCase()
//                    .equals(chatMessage.getChannelName()
//                            .replace
//                                    ("#", "")))
//            {
//                ChatObserver observer = (ChatObserver) pair.getValue();
//                observer._onMessage(chatMessage);
//                return;
//            }
//        }
//
//    }
//
//    public void _leaveChannel(TwitchTVChannel channel) {
//        LOGGER.info("Gonna Disconnect from :" + channel.getChannelName());
//        //this.partChannel("#" + channel.getChannelName());
//        this.sendRawLineViaQueue("PART #" + channel.getChannelName().toLowerCase());
//        observers.remove(channel);
//    }
//    public boolean _isConnected() {
//        return this.isConnected();
//    }
//
//    public static IChat getInstance() throws IOException {
//        if(instance == null)
//            return new TwitchImplOLD();
//        return instance;
//    }
//
//    @Override
//    public String _getUsername() {
//        return this.username;
//    }
//
//    @Override
//    public String getChannelName() {
//        //TODO was �berlegen
//        return null;
//    }
//
//    @Override
//    public String getChannelMap() {
//        StringBuilder builder =new StringBuilder(getVersion());
//        builder.append("\n" + getServer());
//        Iterator it = observers.entrySet().iterator();
//        for(String channel : getChannels())
//        {
//            builder.append("\n|_" + channel);
//        }
//
//        return builder.toString();
//    }
//
//
//    @Override
//    protected void onMessage(String s, String s1, String s2, String s3, String s4) {
//        super.onMessage(s, s1, s2, s3, s4);
//        Iterator it = observers.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry pair = (Map.Entry)it.next();
//            TwitchTVChannel channel = (TwitchTVChannel) pair.getKey();
//            if(s != null && channel.getChannelName().toLowerCase().equals(s
//                    .replace
//                    ("#","")))
//            {
//                ChatObserver observer = (ChatObserver) pair.getValue();
//               // observer._onMessage(s1,s4);
//                return;
//            } else
//            {
//                Iterator it2 = observers.entrySet().iterator();
//                while (it2.hasNext()) {
//                    Map.Entry pair2 = (Map.Entry)it.next();
//                    ChatObserver observer = (ChatObserver) pair.getValue();
//               //     observer._onMessage("Private Message from: " + s1,s4);
//                }
//            }
//        }
//    }
//
//
//    @Override
//    protected void onDisconnect() {
//        super.onDisconnect();
//        Iterator it = observers.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry pair = (Map.Entry)it.next();
//                ChatObserver observer = (ChatObserver) pair.getValue();
//                observer._onMessage(new ChatMessage("Server","Disconnected"));
//        }
//    }


}
