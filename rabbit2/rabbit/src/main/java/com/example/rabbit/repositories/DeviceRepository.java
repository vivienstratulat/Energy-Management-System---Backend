package com.example.rabbit.repositories;

import com.example.rabbit.entities.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device, Integer> {
    Device getDeviceById(int id);
}
