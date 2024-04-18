package com.ems.device.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Entity(name = "Device")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Device  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "max_hourly_consumption")
    private int maxHourlyConsumption;

     @Column(name = "client_id" , nullable = false)
    private UUID clientId;


}

