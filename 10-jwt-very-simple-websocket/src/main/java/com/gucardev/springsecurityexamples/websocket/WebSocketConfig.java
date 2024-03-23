package com.gucardev.springsecurityexamples.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
  private final WebSocketSessionService sessionService;

  public WebSocketConfig(WebSocketSessionService sessionService) {
    this.sessionService = sessionService;
  }

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(new CustomWebSocketHandler(sessionService), "/ws")
            .setAllowedOriginPatterns("*");
  }
}
