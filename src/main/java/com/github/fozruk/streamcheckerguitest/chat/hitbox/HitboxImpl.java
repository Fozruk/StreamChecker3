package com.github.fozruk.streamcheckerguitest.chat.hitbox;

import com.github.epilepticz.streamchecker.exception.ReadingWebsiteFailedException;
import com.github.epilepticz.streamchecker.model.channel.impl.AbstractChannel;
import com.github.fozruk.streamcheckerguitest.chat.ChatObserver;
import com.github.fozruk.streamcheckerguitest.chat.IChat;
import com.github.fozruk.streamcheckerguitest.persistence.PersistedSettingsManager;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Philipp on 16.08.2015.
 *
 * Unfinished
 */
public class HitboxImpl extends WebSocketClient implements IChat {

    private String username;
    private String pw;
    private Map<AbstractChannel,ChatObserver> observers = new HashMap<>();


    public HitboxImpl(URI serverURI) throws JSONException, URISyntaxException, IOException {
        super(new URI("ws://" + getIP() + "/socket.io/1/websocket/" + getID
                        (getIP())),
                new Draft_10());
        this.username = PersistedSettingsManager.getInstance().getValue
                ("hitboxUsername");

        this.pw = PersistedSettingsManager.getInstance().getValue
                ("hitboxPassword");
    }

//    @Override
//    public void _joinChannel(TwitchTVChannel channel, ChatObserver observer) {
//
//        this.send("5:::{\"name\":\"message\"," +
//                "\"args\":[{\"method\":\"joinChannel\"," +
//                "\"params\":{\"channel\":\"" + channel.getChannelName() +
//                "\",\"name\":\"" + username + "\",\"token\":\"" + getToken(username,
//                this.pw) + "\",\"isAdmin\":false}}]}");
//        observers.put(channel,observer);
//        System.out.println("Joined Channel: " + channel.getChannelName());
//    }

//    @Override
//    public void _leaveChannel(TwitchTVChannel channel) {
//
//    }

    @Override
    public void _sendMessage(String channelname, String message) {
        this.send("5:::{\"name\":\"message\"," +
                "\"args\":[{\"method\":\"chatMsg\"," +
                "\"params\":{\"channel\":\"" + channelname + "\",\"name\":\""
                + username + "\",\"nameColor\":\"FA5858\",\"text\":\"" + message +
                "\"}}]}");

    }

    //IChat

    @Override
    public boolean _isConnected() {
        return false;
    }

    @Override
    public String getChannelName() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public void disconnect() {

    }

    @Override
    public String[] getUserList() {
        return null;
    }

    @Override
    public void start() throws IOException, ReadingWebsiteFailedException, JSONException {

    }

    @Override
    public void setObserver(ChatObserver observer) {

    }

    //Websocket

    @Override
    public void onOpen(ServerHandshake handshakedata) {

    }

    @Override
    public void onMessage(String message) {

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {

    }

    @Override
    public void onError(Exception ex) {

    }


    //Hitbox API shizzle

    private static String token;
    private static String getToken(String name, String pass){
        try {
            URL url = new URL("http://api.hitbox.tv/auth/token");
            URLConnection connection = url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            try (DataOutputStream os = new DataOutputStream(connection.getOutputStream ())) {
                String content = "login=" + name + "&pass=" + pass;
                os.writeBytes(content);
                os.flush();
            }
            try (DataInputStream is = new DataInputStream(connection.getInputStream ())) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                token = new JSONObject(reader.readLine()).get("authToken").toString();
                System.out.println(token);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return token;
    }

    private static String getIP() throws JSONException {
        JSONArray arr = new JSONArray(readUrl("http://api.hitbox.tv/chat/servers.json?redis=true"));
        return arr.getJSONObject(0).getString("server_ip");
    }

    private static String getID(String IP){
        String connectionID = readUrl("http://" + IP + "/socket.io/1/");
        String ID = connectionID.substring(0, connectionID.indexOf(":"));
        return ID;
    }

    public static String readUrl(String urlString) {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1) {
                buffer.append(chars, 0, read);
            }
            reader.close();
            return buffer.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }





}
