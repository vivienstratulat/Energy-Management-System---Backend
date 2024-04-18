package com.example.rabbit.services;

import com.example.rabbit.entities.JSONMeasurement;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendNotification(JSONMeasurement measurement, UUID clientId) {
        System.out.println("Sending notification to client: " + clientId);
        messagingTemplate.convertAndSend("/queue/"+clientId,measurement);
    }
}
