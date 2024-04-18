package com.ems.device.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DeviceDetailsDTO {
    private int id;

    private String description;
    private String address;
    private int maxHourlyConsumption;
    private UUID clientId;

}