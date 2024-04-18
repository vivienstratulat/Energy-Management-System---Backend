package com.example.rabbit.services;

import com.example.rabbit.entities.Device;
import com.example.rabbit.entities.builders.DeviceBuilder;
import com.example.rabbit.entities.dtos.DeviceDTO;
import com.example.rabbit.repositories.DeviceRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;

    public Device getDevice(int id) throws Exception {
        Optional<Device> device = Optional.ofNullable(deviceRepository.getDeviceById(id));
        if (!device.isPresent()) {
            throw new Exception("Device not found");
        }
       // return DeviceBuilder.toDeviceDTO(device.get());
        return device.get();
    }

    public int insert(Device device) throws JsonProcessingException {

        device = deviceRepository.save(device);
        System.out.println("Device with id {} was inserted in RABBIT db"+ device.getId());
        System.out.println("Device with "+ device.getId() +" was inserted in db");
        return device.getId();
    }




}
