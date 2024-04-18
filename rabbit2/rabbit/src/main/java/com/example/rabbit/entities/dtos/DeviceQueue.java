package com.example.rabbit.entities.dtos;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@RequiredArgsConstructor
public class DeviceQueue {
    private int id;
    private String description;
    private String address;
    private int maxHourlyConsumption;
    private UUID clientId;
}
