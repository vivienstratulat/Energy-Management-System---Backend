package com.example.rabbit.controller;

import com.example.rabbit.entities.dtos.DailyEnergyConsumptionDTO;
import com.example.rabbit.services.DailyEnergyConsumptionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RabbitController {

    private static final Logger log = LoggerFactory.getLogger(RabbitController.class);

    private final DailyEnergyConsumptionService dailyEnergyConsumptionService;

    @GetMapping("/measures/{deviceId}")
    public ResponseEntity<DailyEnergyConsumptionDTO> getMeasures(
            @PathVariable int deviceId,
            @RequestParam String date) {

        // Parse the date string into a LocalDate
        LocalDate localDate = LocalDate.parse(date);

        // Call the service method to get daily energy consumption
        DailyEnergyConsumptionDTO result = dailyEnergyConsumptionService.getMeasures(deviceId, localDate);

        // Return the result as ResponseEntity
        System.out.println("THIS IS THE RESUL GRAPH: " + result);
        return ResponseEntity.ok(result);
    }


}
