package com.gucardev.springsecurityexamples.websocket;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class WebSocketSessionService {

    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public void addSession(String user, WebSocketSession session) {
        sessions.put(user, session);
    }

    public void removeSession(String user) {
        sessions.remove(user);
    }

    public WebSocketSession getSession(String user) {
        return sessions.get(user);
    }

}
