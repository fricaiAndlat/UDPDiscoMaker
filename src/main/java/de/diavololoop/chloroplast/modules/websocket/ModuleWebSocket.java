package de.diavololoop.chloroplast.modules.websocket;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import de.diavololoop.chloroplast.DiscoMaker;
import de.diavololoop.chloroplast.effect.Effect;
import de.diavololoop.chloroplast.modules.Module;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Chloroplast
 *
 * Module for creating a WebSocket Server. Can change Effects.
 */
public class ModuleWebSocket extends Module {

    private EffectWebSocket webSocket;

    private List<WebSocket> connections;
    private Gson gson;
    private int port;

    public ModuleWebSocket(DiscoMaker program){
        super(program);
    }


    @Override
    public String getKey() {
        return "-ws";
    }

    @Override
    public int getParameterCount() {
        return 1;
    }

    @Override
    public String init(String[] args) {
        markLoaded();

        String portString = args[0].trim();

        if(!portString.matches("\\d{1,5}")){
            return "cant start websocket, given port is not valid: "+portString;
        }

        port = Integer.parseInt(portString);
        return null;
    }

    @Override
    public String onStart(File root) {
        connections = new LinkedList<>();
        gson = new Gson();

        webSocket = new EffectWebSocket(port);
        webSocket.start();

        return null;
    }

    @Override
    public void onQuit() {
        try {
            webSocket.stop();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void onEffectChange(Effect effect) {
        synchronized (connections) {
            EffectNotification notification = new EffectNotification(effect);
            String message = gson.toJson(notification);
            connections.forEach(con -> con.send(message));
        }
    }

    private class EffectWebSocket extends WebSocketServer  {

        private EffectWebSocket(int port){
            super(new InetSocketAddress(port));
        }

        @Override
        public void onOpen(WebSocket conn, ClientHandshake handshake) {
            synchronized (connections) {
                connections.add(conn);
            }
        }

        @Override
        public void onClose(WebSocket conn, int code, String reason, boolean remote) {
            synchronized (connections) {
                connections.remove(conn);
            }
        }

        @Override
        public void onMessage(WebSocket conn, String message) {


            try {

                Request request = gson.fromJson(message, Request.class);

                if(request.type.equalsIgnoreCase("effect")){
                    Effect effect = program().getEffectLoader().allEffects().get(request.name);

                    if(effect == null) {
                        Request answer = new Request("error", "effect was not found", null);
                        conn.send(gson.toJson(answer));
                        return;
                    }

                    program().setEffect(effect, request.args);

                    EffectNotification notification = new EffectNotification(effect);
                    connections.forEach(con -> con.send(gson.toJson(notification)));
                }



            } catch (JsonSyntaxException e){
                Request errorAnswer = new Request("error", "json syntax was not valid", null);
                conn.send(gson.toJson(errorAnswer));
            }


        }

        @Override
        public void onError(WebSocket conn, Exception ex) {
            synchronized (connections) {
                connections.remove(conn);
            }
            ex.printStackTrace();
        }

        @Override
        public void onStart() {

        }
    }

    private class EffectNotification {

        String type;
        String name;
        String description;
        String author;

        public EffectNotification(Effect effect){
            this.type = "effect";
            this.name = effect.getName();
            this.description = effect.getDescription();
            this.author = effect.getAuthor();
        }

    }

    private class Request {
        String type;
        String name;
        String args;

        public Request(String type, String name, String args) {
            this.type = type;
            this.name = name;
            this.args = args;
        }
    }
}
