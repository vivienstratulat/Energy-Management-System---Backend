package com.example.rabbit.services;

import com.example.rabbit.entities.DailyEnergyConsumption;
import com.example.rabbit.entities.Device;
import com.example.rabbit.entities.JSONMeasurement;
import com.example.rabbit.entities.dtos.DailyEnergyConsumptionDTO;
import com.example.rabbit.repositories.DailyEnergyConsumptionRepository;
import com.example.rabbit.repositories.DeviceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DailyEnergyConsumptionService {
    private final DailyEnergyConsumptionRepository dailyEnergyConsumptionRepository;
    private final DeviceRepository deviceRepository;

    public DailyEnergyConsumptionDTO save(DailyEnergyConsumption dailyEnergyConsumption) {
        // Check if a record with the same device ID already exists
       /* DailyEnergyConsumption existingRecord = dailyEnergyConsumptionRepository.findByDeviceId(dailyEnergyConsumption.getDeviceId());

        if (existingRecord != null) {
            // Update existing record
            existingRecord.setConsumption(dailyEnergyConsumption.getConsumption());
            existingRecord.setTimeStamp(dailyEnergyConsumption.getTimeStamp());
            existingRecord = dailyEnergyConsumptionRepository.save(existingRecord);

            DailyEnergyConsumptionDTO existingRecordDTO = DailyEnergyConsumptionBuilder.toDailyEnergyDTO(existingRecord);
         //   existingRecordDTO.setDevice(DeviceBuilder.toDeviceDTO(existingRecord.getDevice()));
            return existingRecordDTO;
        } else {
            // Insert new record
            DailyEnergyConsumption newRecord = dailyEnergyConsumptionRepository.save(dailyEnergyConsumption);
            DailyEnergyConsumptionDTO newRecordDTO = DailyEnergyConsumptionBuilder.toDailyEnergyDTO(newRecord);
           // newRecordDTO.setDevice(DeviceBuilder.toDeviceDTO(newRecord.getDevice()));
            return newRecordDTO;
        }*/
        return null;
    }

    public boolean handleMeasure(DailyEnergyConsumption message) {
        Device device = insert(message);
      return  checkHourlyConsumption(device);
    }

    public Device insert(DailyEnergyConsumption dailyEnergyConsumption) {
        try {
            // Calculate and adjust the consumption
            Float sumOfPreviousMeasures = dailyEnergyConsumptionRepository
                    .findAllByDeviceId(dailyEnergyConsumption.getDeviceId()).stream()
                    .map(DailyEnergyConsumption::getConsumption)
                    .reduce(0.0F, Float::sum);

            dailyEnergyConsumption.setConsumption(dailyEnergyConsumption.getConsumption() - sumOfPreviousMeasures);

            // Save daily energy consumption
            dailyEnergyConsumptionRepository.save(dailyEnergyConsumption);
            System.out.println("Daily energy consumption saved");

            // Update device measure_number
            Device device = deviceRepository.getDeviceById(dailyEnergyConsumption.getDeviceId());

            if (device != null) {
                device.setMeasure_number(device.getMeasure_number() + 1);
                deviceRepository.save(device);
                System.out.println("Measure number updated: " + device.getMeasure_number());
                return device;
            } else {
                // Handle the situation when the device does not exist
                // You might want to log a warning or throw an exception
                System.out.println("Device not found for ID: " + dailyEnergyConsumption.getDeviceId());
                // You can throw an exception or return null based on your error-handling strategy
                throw new EntityNotFoundException("Device not found for ID: " + dailyEnergyConsumption.getDeviceId());
            }
        } catch (DataIntegrityViolationException e) {
            // Handle duplicate key violation
            // You might want to log a warning or throw a custom exception
            System.out.println("Error saving daily energy consumption: " + e.getMessage());
            throw new RuntimeException("Error saving daily energy consumption", e);
        }
    }

    private Boolean checkHourlyConsumption(Device device) {
        if (device.getMeasure_number() % 6 == 0) {
            System.out.println("Checking the consumption from last hour...");
          Float hourlyConsumption = dailyEnergyConsumptionRepository
                    .findAll(PageRequest.of(0, 6, Sort.by("consumption").descending()))
                    .stream()
                    .map(DailyEnergyConsumption::getConsumption)
                    .reduce(0.0F, Float::sum);

            if (hourlyConsumption > device.getMaxHourlyConsumption()) {
                System.out.println("Hourly consumption exceeded the limit: " + hourlyConsumption);
                return true;
            }
        }
        return false;
    }

    public DailyEnergyConsumptionDTO getMeasures(int deviceId, LocalDate date) {
        Pageable pageable = PageRequest.of(0, 6, Sort.by("timestamp").ascending());

        List<Float> xValues = new ArrayList<>();
        List<Float> yValues = new ArrayList<>();

        int counter = 0;

        Page<DailyEnergyConsumption> result;
        do {
            result = dailyEnergyConsumptionRepository.findAllByDeviceIdAndTimestampBetween(
                    deviceId,
                    LocalDateTime.of(date, LocalTime.MIN),
                    LocalDateTime.of(date, LocalTime.MAX),
                    pageable);

           // System.out.println("Result: " + result.getContent());

            yValues.add(result.stream()
                    .map(DailyEnergyConsumption::getConsumption)
                    .reduce(0.0F, Float::sum));
            counter++;

            pageable = result.nextPageable();
        } while (result.hasNext());

        for (int i = 0; i < counter; i++) {
            xValues.add((float) i);
            //System.out.println("XXXXX"+xValues.get(i));
        }

        System.out.println("X values: " + xValues);
        System.out.println("Y values: " + yValues);
        return DailyEnergyConsumptionDTO.builder().xValues(xValues).yValues(yValues).build();
    }
}
