package com.example.rabbit.services;

import com.example.rabbit.entities.Device;
import com.example.rabbit.entities.dtos.DeviceDTO;
import org.mapstruct.Mapper;

@Mapper
public interface DeviceMapper {
    Device toEntity(DeviceDTO deviceDTO);
    DeviceDTO toDTO(Device device);

}
