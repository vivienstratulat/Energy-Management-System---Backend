package com.example.rabbit.rabbitmq;

import com.example.rabbit.entities.DailyEnergyConsumption;
import com.example.rabbit.entities.Device;
import com.example.rabbit.entities.JSONMeasurement;
import com.example.rabbit.services.DailyEnergyConsumptionService;
import com.example.rabbit.services.DeviceService;
import com.example.rabbit.services.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitConsumer {
    private final DeviceService deviceService;
    private final DailyEnergyConsumptionService dailyEnergyConsumptionService;
    private final SimpMessagingTemplate messagingTemplate;

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper = new ObjectMapper(); // Inject ObjectMapper

  int contor=0;
    float sum=0;


    public RabbitConsumer(DeviceService deviceService, DailyEnergyConsumptionService dailyEnergyConsumptionService, SimpMessagingTemplate messagingTemplate, NotificationService notificationService) {
        this.deviceService = deviceService;
        this.dailyEnergyConsumptionService = dailyEnergyConsumptionService;
        this.messagingTemplate = messagingTemplate;
        this.notificationService = notificationService;
    }

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void onMessage(String jsonMessage) {
        try {
            System.out.println("Received raw message: " + jsonMessage);

            JSONMeasurement measurement = objectMapper.readValue(jsonMessage, JSONMeasurement.class);

            DailyEnergyConsumption dailyEnergyConsumption = new DailyEnergyConsumption();
            dailyEnergyConsumption.setId((int) Math.random());

            dailyEnergyConsumption.setTimestamp(measurement.getTimeStamp());

            dailyEnergyConsumption.setDeviceId(measurement.getDeviceId());

            //LOGICA PT CALCUL HOURLY CONSUMPTION
          /*  float value=0;
            System.out.println("Suma este: "+sum);

            if(contor<=5)
            {
                 value=measurement.getMeasurementValue();
                System.out.println("value= "+value);
                System.out.println("contor= "+contor);


                contor++;
            }
            System.out.println("value afara= "+value);

            if(contor==5)
            {
                sum=value;
            }
            System.out.println("Suma este afrar: "+sum);
*/
            dailyEnergyConsumption.setConsumption(measurement.getMeasurementValue());

          //  dailyEnergyConsumptionService.save(dailyEnergyConsumption);

           /* if(dailyEnergyConsumption.getConsumption()>dailyEnergyConsumption.getDevice().getMaxHourlyConsumption()){
                System.out.println("Overconsumption detected at device !" + dailyEnergyConsumption.getDevice().getId() );
                measurement.setFlag(1);
               // messagingTemplate.convertAndSend("/queue", "Overconsumption detected at device " + dailyEnergyConsumption.getDevice().getId());
                messagingTemplate.convertAndSend("/queue",measurement);

            }*/

            try {
                Boolean fl= dailyEnergyConsumptionService.handleMeasure(dailyEnergyConsumption);
                if(fl==true)
                {
                    System.out.println("Overconsumption detected at device !" + dailyEnergyConsumption.getDeviceId());
                    measurement.setFlag(1);
                    // messagingTemplate.convertAndSend("/queue", "Overconsumption detected at device " + dailyEnergyConsumption.getDevice().getId());
                   // messagingTemplate.convertAndSend("/queue/",measurement);
                    Device device=deviceService.getDevice(dailyEnergyConsumption.getDeviceId());
                    notificationService.sendNotification(measurement,device.getClientId());
                }

            }
            catch (Exception e) {
                System.out.println("Error saving daily energy consumption: " + e.getMessage());
            }
          //  System.out.println("Received message: " + savedDailyEnergyConsumption.getConsumption());
        } catch (Exception e) {
            e.printStackTrace();
            // Log additional information if needed
        }
    }
}
