package com.example.rabbit.entities.builders;

import com.example.rabbit.entities.Device;
import com.example.rabbit.entities.dtos.DeviceDTO;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DeviceBuilder {

    public static DeviceDTO toDeviceDTO(Device device) {
        return new DeviceDTO(device.getId(),
                device.getMaxHourlyConsumption(),device.getClientId(),device.getMeasure_number());
    }

    public static Device toEntity(DeviceDTO deviceDTO) {
        return new Device(deviceDTO.getId(),
                deviceDTO.getMaxHourlyConsumption(),deviceDTO.getClientId(),deviceDTO.getMeasure_number());
    }
}
