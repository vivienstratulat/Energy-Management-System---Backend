package com.ems.device.controller;

import com.ems.device.dto.DeviceDetailsDTO;
import com.ems.device.entities.Device;
import com.ems.device.rabbit.RabbitProducer;
import com.ems.device.service.DeviceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/device")
@CrossOrigin(origins = "http://localhost:3000")
public class DeviceController {

    private final DeviceService deviceService;



    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<DeviceDetailsDTO>> getDevices() {
        List<DeviceDetailsDTO> dtos = deviceService.findDevices();
        /*for (DeviceDetailsDTO dto : dtos) {
            Link personLink = linkTo(methodOn(DeviceController.class)
                    .getDevice(dto.getId())).withRel("deviceDetails");
            dto.add(personLink);
        }*/
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping("/register/{id}")
    public ResponseEntity<Integer> insertDevice(@RequestBody DeviceDetailsDTO deviceDTO,@PathVariable("id") UUID clientId) throws JsonProcessingException {
        int deviceID = deviceService.insert(deviceDTO,clientId);
        System.out.println("INSERT DEVICE");
        return new ResponseEntity<>(deviceID, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<DeviceDetailsDTO> getDevice(@PathVariable("id")int deviceId) {
        DeviceDetailsDTO dto = deviceService.findDeviceById(deviceId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    ResponseEntity<Integer> updateDevice(@PathVariable("id") int deviceId, @RequestBody DeviceDetailsDTO deviceDTO) {
        int deviceID = deviceService.update(deviceId,deviceDTO);
        return new ResponseEntity<>(deviceID, HttpStatus.OK);
    }

    @PostMapping("/delete/{id}")
    ResponseEntity<Integer> deleteDevice(@PathVariable("id") int deviceId) {
        int deviceID = deviceService.delete(deviceId);
        return new ResponseEntity<>(deviceID, HttpStatus.OK);

    }

    @PostMapping("/deleteWithClient/{id}")
    ResponseEntity<UUID> deleteDeviceWithClient(@PathVariable("id") UUID deviceId) {
        UUID deviceID = deviceService.deleteWithClient(deviceId);
        return new ResponseEntity<>(deviceID, HttpStatus.OK);

    }
    @GetMapping("/getByClientId/{id}")
    public ResponseEntity<List<Device>> getDeviceByClientId(@PathVariable("id") UUID clientId) {
        List<Device> dto = deviceService.findDeviceByClientId(clientId);
        return  ResponseEntity.ok(dto);
    }


}
