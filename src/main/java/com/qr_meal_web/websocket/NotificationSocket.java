package com.qr_meal_web.websocket;


import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint("/ws/notify")
public class NotificationSocket {

    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        System.out.println("📩 Message from client: " + message);
        broadcast("📢 Broadcast from " + session.getId() + ": " + message);
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        System.out.println("❌ Client disconnected: " + session.getId());
    }

    private void sendMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    // Gửi tin nhắn đến tất cả client
    public static void broadcast(String message) {
        synchronized (sessions) {
            for (Session s : sessions) {
                try {
                    s.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}