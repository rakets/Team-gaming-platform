package com.project.gamingplatform.service;

import io.livekit.server.AccessToken;
import io.livekit.server.RoomJoin;
import io.livekit.server.RoomName;
import io.livekit.server.WebhookReceiver;
import livekit.LivekitWebhook;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LiveKitService {
    @Value("${livekit.api.key}")
    private String LIVEKIT_API_KEY;

    @Value("${livekit.api.secret}")
    private String LIVEKIT_API_SECRET;

    public AccessToken createToken(String roomName, String participantName){
        AccessToken token = new AccessToken(LIVEKIT_API_KEY, LIVEKIT_API_SECRET);
        token.setName(participantName);
        token.setIdentity(participantName);
        token.addGrants(new RoomJoin(true), new RoomName(roomName));

        log.info("LiveKit token has been created: {}", token);

        return token;
    }

    public ResponseEntity<String> receiveWebhook(String authHeader, String body) {
        WebhookReceiver webhookReceiver = new WebhookReceiver(LIVEKIT_API_KEY, LIVEKIT_API_SECRET);
        try {
            LivekitWebhook.WebhookEvent event = webhookReceiver.receive(body, authHeader);
            log.info("LiveKit Webhook type={}, room={}",
                    event.toString(),
                    event.getRoom() != null ? event.getRoom().getName() : "n/a");

            return ResponseEntity.ok("ok");

        } catch (IllegalArgumentException e) {
            // ошибка валидации / подписи / payload
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("invalid webhook");
        } catch (SecurityException e) {
            // неверная подпись Authorization
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("unauthorized");
        } catch (Exception e) {
            // любая другая ошибка
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("error");
        }
    }
}