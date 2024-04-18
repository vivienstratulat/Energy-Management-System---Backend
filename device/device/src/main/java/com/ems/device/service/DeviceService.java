package com.ems.device.service;

import com.ems.device.dto.DeviceDetailsDTO;
import com.ems.device.entities.Device;
import com.ems.device.mappers.DeviceMapper;
import com.ems.device.rabbit.RabbitProducer;
import com.ems.device.repositories.DeviceRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DeviceService {
    private final DeviceRepository deviceRepository;
    //
    private final DeviceMapper deviceMapper= Mappers.getMapper(DeviceMapper.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitProducer producer;

    private ObjectMapper objectMapper = new ObjectMapper();

    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public List<DeviceDetailsDTO> findDevices() {
        List<Device> personList = deviceRepository.findAll();
        return personList.stream()
                .map(deviceMapper::toDTO)
                .collect(Collectors.toList());
    }

    public DeviceDetailsDTO findDeviceById(int id) {
        Optional<Device> prosumerOptional = deviceRepository.findById(id);
        if (!prosumerOptional.isPresent()) {
            log.error("Device with id {} was not found in db", id);
        }
        System.out.println("Device with "+ id +" was found in db");
        return deviceMapper.toDTO(prosumerOptional.get());
    }

   /* public UUID insert(DeviceDetailsDTO deviceDTO) {
        Device device = DeviceBuilder.toEntity(deviceDTO);
        device = deviceRepository.save(device);
        log.debug("Device with id {} was inserted in db", device.getId());
        System.out.println("Device with "+ device.getId() +" was inserted in db");
        return device.getId();
    }*/

    public int update(int deviceID, DeviceDetailsDTO deviceDTO) {
        Optional<Device> personOptional = deviceRepository.findById((deviceID));
        if (!personOptional.isPresent()) {
            log.error("Device with id {} was not found in db", deviceID);
        }

        Device existingDevice = personOptional.get();
        existingDevice.setAddress(deviceDTO.getAddress());
        existingDevice.setDescription(deviceDTO.getDescription());
        existingDevice.setMaxHourlyConsumption(deviceDTO.getMaxHourlyConsumption());

        existingDevice = deviceRepository.save(existingDevice);

        log.debug("Device with id {} was updated in db", existingDevice.getId());
        System.out.println("Device with " + existingDevice.getId() + " was updated in db");

        return existingDevice.getId();
    }

    public int delete(int deviceId){
        Optional<Device> personOptional = deviceRepository.findById(deviceId);
        if (!personOptional.isPresent()) {
            log.error("Device with id {} was not found in db", deviceId);
        }
        deviceRepository.deleteById(deviceId);
        log.debug("Device with id {} was deleted from the db", deviceId);
        System.out.println("Device with " + deviceId + " was deleted from the db");
        return deviceId;
    }

    public int insert(DeviceDetailsDTO deviceDTO, UUID clientId) throws JsonProcessingException {
        Device device = deviceMapper.toDevice(deviceDTO);
        device.setClientId(clientId);
        System.out.println(deviceDTO.getMaxHourlyConsumption());
        device = deviceRepository.save(device);


        try{
        producer.produceMessage(objectMapper.writeValueAsString(device));
            System.out.println(device.getDescription());

        }
        catch (Exception e){
            System.out.println("Error in sending message in service");
        }

        log.debug("Device with id {} was inserted in db", device.getId());
        System.out.println("Device with "+ device.getId() +" was inserted in db");
        return device.getId();
    }

    public List<Device> findDeviceByClientId(UUID clientId) {
        List<Device> prosumerOptional = deviceRepository.findByClientId(clientId);
        if (prosumerOptional == null) {
            log.error("Device with clientId {} was not found in db", clientId);
        }
        System.out.println("Device with "+ clientId +" was found in db");
        return prosumerOptional;

    }

    public UUID deleteWithClient(UUID cliendId){
        List<Device> devices = deviceRepository.findByClientId(cliendId);
        for(Device device: devices){
            deviceRepository.deleteById(device.getId());
        }
        log.debug("Device with clientId {} was deleted from the db", cliendId);
        System.out.println("Device with " + cliendId + " was deleted from the db");
        return cliendId;
    }



}
