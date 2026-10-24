package com.untitled.hermesapi.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@SequenceGenerator(name = "users_seq", sequenceName = "users_seq", allocationSize = 10)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID, generator = "users_seq")
    private UUID id;

    @Column(nullable = false, unique = true, length = 100)
    private String key;

    @Column(nullable = false, unique = true, length = 100)
    private String accountNumber;

    private boolean isEnabled = false;

    private boolean isAccountLocked = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime lastloginAt;
}
