package com.project.gamingplatform.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
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

                // Если это команда CONNECT
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    // 1. Достаем native-заголовок "roomId"
                    String roomId = accessor.getFirstNativeHeader("roomId");

                    // 2. Кладем его в атрибуты сессии Spring
                    if (roomId != null) {
                        accessor.getSessionAttributes().put("roomId", roomId);
                    }
                }
                return message;
            }
        });
    }
}
