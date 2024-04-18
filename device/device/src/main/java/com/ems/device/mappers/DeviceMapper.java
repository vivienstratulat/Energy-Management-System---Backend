package com.ems.device.mappers;

import com.ems.device.dto.DeviceDetailsDTO;
import com.ems.device.entities.Device;
import org.mapstruct.Mapper;

@Mapper
public interface DeviceMapper {
    DeviceDetailsDTO toDTO(Device device);
    Device toDevice(DeviceDetailsDTO deviceDetailsDTO);
}
