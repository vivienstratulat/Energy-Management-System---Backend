package com.example.rabbit.repositories;
import com.example.rabbit.entities.DailyEnergyConsumption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface DailyEnergyConsumptionRepository extends JpaRepository<DailyEnergyConsumption, Integer> {

    //DailyEnergyConsumption findByDeviceId(Integer deviceId);
    List<DailyEnergyConsumption> findAllByDeviceId(Integer deviceId);
   Page<DailyEnergyConsumption> findAllByDeviceIdAndTimestampBetween(Integer deviceId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

}
