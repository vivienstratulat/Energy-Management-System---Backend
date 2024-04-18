package com.ems.device.repositories;
import com.ems.device.entities.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device, Integer> {
    List<Device> findByClientId(UUID clientId);
}
