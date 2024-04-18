package com.example.rabbit.rabbitmq;

import com.example.rabbit.entities.DailyEnergyConsumption;
import com.example.rabbit.entities.Device;
import com.example.rabbit.entities.JSONMeasurement;
import com.example.rabbit.entities.dtos.DailyEnergyConsumptionDTO;
import com.example.rabbit.entities.dtos.DeviceQueue;
import com.example.rabbit.services.DailyEnergyConsumptionService;
import com.example.rabbit.services.DeviceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitDeviceConsumer {

    private final DeviceService deviceService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper(); // Inject ObjectMapper

    public RabbitDeviceConsumer(DeviceService deviceService, SimpMessagingTemplate messagingTemplate) {
        this.deviceService = deviceService;
        this.messagingTemplate = messagingTemplate;
    }

    @RabbitListener(queues = "${rabbitmq.queue.name2}")
    public void onMessage(String jsonMessage) {
        try {
            System.out.println("DEVICE Received raw message: " + jsonMessage);

            DeviceQueue device = objectMapper.readValue(jsonMessage, DeviceQueue.class);

            Device dev2=new Device();
            dev2.setId(device.getId());
            dev2.setClientId(device.getClientId());
            dev2.setMeasure_number(0);
            dev2.setMaxHourlyConsumption(device.getMaxHourlyConsumption());

            try {
                int d = deviceService.insert(dev2);
            }
            catch (Exception e) {
                System.out.println("Error saving device: " + e.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Log additional information if needed
        }
    }
}
