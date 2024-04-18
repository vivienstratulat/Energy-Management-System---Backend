package com.example.chat.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class Message {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "fromId", nullable = false)
    private int fromId;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "seen", nullable = false)
    private boolean seen;

    @Column(name = "toId", nullable = false)
    private int toId; // for direct messaging to the admin

    @Column(name = "timestamp", nullable = false, updatable = false)
    private LocalDateTime timestamp;

    // getters and setters

}

