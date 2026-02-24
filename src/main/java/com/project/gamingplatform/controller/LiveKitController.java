package com.project.gamingplatform.controller;

import com.project.gamingplatform.service.LiveKitService;
import io.livekit.server.AccessToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@Slf4j
public class LiveKitController {
    private final LiveKitService liveKitService;

    public LiveKitController(LiveKitService liveKitService) {
        this.liveKitService = liveKitService;
    }

    @PostMapping(value = "/token")
    public ResponseEntity<Map<String, String>> createToken(@RequestBody Map<String, String> params) {
        String roomName = params.get("roomName");
        String participantName = params.get("participantName");

        log.info("User: {} from room: {} send request to create LiveKit token.", participantName, roomName);

        if (roomName == null || participantName == null) {
            return ResponseEntity.badRequest().body(Map.of("errorMessage", "roomName and participantName are required"));
        }
        AccessToken LiveKitToken = liveKitService.createToken(roomName, participantName);
        return ResponseEntity.ok(Map.of("token", LiveKitToken.toJwt()));
    }

    @PostMapping(value = "/livekit/webhook", consumes = "application/webhook+json")
    public ResponseEntity<String> receiveWebhook(@RequestHeader(value = "Authorization", required = false) String authHeader, @RequestBody String body) {
        // если не авторизован
        if(authHeader == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("unauthorized");
        }
        return liveKitService.receiveWebhook(authHeader, body);
    }
}
