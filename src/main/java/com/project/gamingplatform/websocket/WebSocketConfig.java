package com.project.gamingplatform.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final WebSocketService webSocketService;

    @Autowired
    public WebSocketConfig(@Lazy WebSocketService webSocketService) { //используем @Lazy, так как WebSocketService зависит от TaskScheduler, который создается здесь
        this.webSocketService = webSocketService;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/WsGameRoom").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // for public
        config.enableSimpleBroker("/topic")
                .setTaskScheduler(heartBeatScheduler()) //добавляем планировщик
                .setHeartbeatValue(new long[]{5000, 5000}); //сервер шлет каждые 5с, Ждет каждые 5с);
        // for request from user to server
        config.setApplicationDestinationPrefixes("/app");
    }

    // Бин планировщика для пингов
    @Bean
    public TaskScheduler heartBeatScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(1); // Достаточно одного потока для пингов
        scheduler.setThreadNamePrefix("wss-heartbeat-thread-");
        scheduler.initialize(); // Важно инициализировать!
        return scheduler;
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                // если это команда CONNECT
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    //достаем native-заголовок "roomId", "userId", "gameRole".
                    String roomIdStr = accessor.getFirstNativeHeader("roomId");
                    String userIdStr = accessor.getFirstNativeHeader("userId");
                    String gameRole = accessor.getFirstNativeHeader("gameRole");
                    Map<String, Object> attributes = accessor.getSessionAttributes();
                    log.info("attributes in 'preSend': " + attributes);
                    // парсим roomId и кладем в сессию Spring
                    if (roomIdStr != null && attributes != null) {
                        try {
                            Integer roomId = Integer.valueOf(roomIdStr);
                            accessor.getSessionAttributes().put("roomId", roomId);
                        } catch (NumberFormatException e) {
                            log.error("Error of parsing roomId: " + roomIdStr);
                        }

                    }

                    // парсим userId и кладем в сессию Spring
                    if (userIdStr != null && attributes != null) {
                        try {
                            Integer userId = Integer.valueOf(userIdStr);
                            accessor.getSessionAttributes().put("userId", userId);

                            webSocketService.cancelPendingRemoval(userId); //Если пользователь подключился (CONNECT), то отменяем его удаление.
                        } catch (NumberFormatException e) {
                            log.error("Error of parsing userId: " + roomIdStr);
                        }
                    }
                    // кладем gameRole в сессию Spring
                    if (gameRole != null && attributes != null) {
                        accessor.getSessionAttributes().put("gameRole", gameRole);
                    }
                }
                return message;
            }
        });
    }
}
