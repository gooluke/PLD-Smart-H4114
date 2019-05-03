/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.insalyon.videostream;
import java.util.logging.Logger;
 
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.websocket.server.PathParam;
import org.json.JSONObject;
/**
 *
 * @author scheah
 */

@ServerEndpoint(value = "/video/{room}/{usertype}")
public class ServerEndPoint {
    private Session session;
    private String roomNumber;
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private static HashMap<String, Set<ServerEndPoint> > serverEndPoints = new HashMap<String, Set<ServerEndPoint> >();
    private static HashMap<String, ServerEndPoint> serverEndPointStart = new HashMap<String, ServerEndPoint>();
    private static HashMap<String, Boolean> serverEndPointState = new HashMap<String, Boolean>();
    private static HashMap<String, String> serverEndPointRoomKey = new HashMap<String, String>();
    
    public static HashMap<String, Boolean> getServerEndPointState(){
        return serverEndPointState;
    }
    
    public static HashMap<String, String> getServerEndPointRoomKey(){
        return serverEndPointRoomKey;
    }
 
    @OnOpen
    public void onOpen(Session session, @PathParam("usertype") String usertype, @PathParam("room") String roomNumber) throws IOException {
        System.out.println("Connected ... " + session.getId());
        logger.info("Connected ... " + session.getId());
        this.session = session;
        this.roomNumber = roomNumber;
        if (usertype.equals("start")) {
            serverEndPoints.put(roomNumber, new CopyOnWriteArraySet<ServerEndPoint> ());
            serverEndPointStart.put(roomNumber, this);
            serverEndPointState.put(roomNumber, Boolean.FALSE);
        }
        else {
            if(serverEndPoints.get(roomNumber).size() < 1){
                serverEndPoints.get(roomNumber).add(this);
                serverEndPointState.replace(roomNumber, Boolean.TRUE);
            }
            
        }
    }
 
    @OnMessage
    public String onMessage(String message, Session session) throws IOException {
        JSONObject json = new JSONObject(message);
        if (json != null) {
            String userType = json.getString("user");
            String type = json.getString("type");
            if ("bye".equals(type)) {
                System.out.println("user :" + session.getId() + " " + userType + " exit..");
                if(userType.equals("start")){
                    serverEndPoints.remove(roomNumber);
                    serverEndPointState.remove(roomNumber);
                }else{
                    serverEndPoints.get(roomNumber).remove(this);
                    serverEndPointState.replace(roomNumber, Boolean.FALSE);
                }
            }else if ("offer".equals(type)){
                if(userType.equals("start")){
                    broadcast(message);
                }else{
                    System.err.println("Wrong userType sent offer ");
                }
            }else if ("answer".equals(type)){
                if(userType.equals("listen")){
                    sendToStart(message);
                }else{
                    System.err.println("Wrong userType sent answer ");
                }
            }else if ("candidate".equals(type)){
                if(userType.equals("start")){
                    broadcast(message);
                }else{
                    System.err.println("Wrong userType sent answer ");
                }
            }
        }
        return message;
    }
 
    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        logger.info(String.format("Session %s closed because of %s", session.getId(), closeReason));
    }
    
    private void broadcast(String message) throws IOException {
        for(ServerEndPoint endpoint : serverEndPoints.get(this.roomNumber)) {
            synchronized (endpoint) {
                try {
                    endpoint.session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void sendToStart(String message) throws IOException {
        ServerEndPoint endpoint = serverEndPointStart.get(this.roomNumber);
        try {
            endpoint.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}