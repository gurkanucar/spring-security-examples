package com.gucardev.springsecurityexamples.websocket;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

@RequiredArgsConstructor
@Slf4j
public class CustomWebSocketHandler extends AbstractWebSocketHandler {
  private final WebSocketSessionService sessionService;

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws IOException {
    var principal = session.getPrincipal();

    if (principal == null || principal.getName() == null) {
      session.close(CloseStatus.SERVER_ERROR.withReason("User must be authenticated"));
      return;
    }

    sessionService.addSession(principal.getName(), session);
    log.info("Connection established with user: {}", principal.getName());
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
    var principal = session.getPrincipal();
    sessionService.removeSession(principal.getName());
    log.info("Connection closed with user: {}", principal.getName());
  }

  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message)
      throws IOException {
    var principal = session.getPrincipal();
    if (principal != null) {
      log.info("Received message from user {}: {}", principal.getName(), message.getPayload());
      // Handle the message as needed
    }
  }
}
