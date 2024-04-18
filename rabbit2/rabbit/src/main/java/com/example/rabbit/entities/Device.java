package com.example.rabbit.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Device {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private int id;

    @Column(name = "max_hourly_consumption")
    private int maxHourlyConsumption;

    @Column(name = "client_id" , nullable = false)
    private UUID clientId;

    @Column(name = "measure_number")
    private int measure_number;

}
