package com.example.chat.entities;

import jakarta.persistence.*;

import java.util.UUID;
@Entity
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "external_id", nullable = false, unique=true)
    private UUID externalId;

    @Column(name = "email")
    private String email;
}
