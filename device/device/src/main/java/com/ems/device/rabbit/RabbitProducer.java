package com.ems.device.rabbit;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RabbitProducer {

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routingkey.name}")
    private String routingKey;

    private final RabbitTemplate rabbitTemplate;

    // ObjectMapper mapper=new ObjectMapper();

    @Autowired
    public RabbitProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;

    }


    public void produceMessage(String message) {
        try {

            if (message == null) {
                System.out.println("Message is null or empty. Not sending.");
                return;
            }
            try {
                rabbitTemplate.convertAndSend(exchange, routingKey, message);
                System.out.println("Message sent" + message);
            } catch (Exception e) {
                System.out.println("Exception in RabbitProducer.produceMessage: " + e.getMessage());
            }
            //log.info("Message sent: {}", message);

        } catch (AmqpException e) {
            //log.error("Error sending message: {}", e.getMessage(), e);
            System.out.println("Error sending message: {}" + e.getMessage());
            // Handle the exception as needed
        }
    }


}
