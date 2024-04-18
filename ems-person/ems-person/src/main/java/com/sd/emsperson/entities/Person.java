package com.sd.emsperson.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Table(name = "person")
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "external_id", nullable = false, unique=true)
    private UUID externalId;

    private String email;

}
