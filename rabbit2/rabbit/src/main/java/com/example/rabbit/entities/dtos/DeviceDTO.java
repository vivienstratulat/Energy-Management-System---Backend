package com.example.rabbit.entities.dtos;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class DeviceDTO {
    private int id;
    private int maxHourlyConsumption;
    private UUID clientId;
    private int measure_number;
}
