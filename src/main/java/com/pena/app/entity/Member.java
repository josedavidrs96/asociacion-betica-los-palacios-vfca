package com.pena.app.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre corto (compatibilidad)
    @Column(nullable = false, length = 180)
    private String name;

    // Nuevo: nombre completo con apellidos
    private String fullName;

    private String phone;

    // Nuevo: email para verificación
    @Column(length = 180)
    private String email;

    // Nuevo: domicilio/dirección
    @Column(length = 255)
    private String address;

    // Nuevo: estado de verificaciones
    @Column(nullable = false, columnDefinition = "INTEGER NOT NULL DEFAULT 0")
    private boolean emailVerified = false;
    @Column(nullable = false, columnDefinition = "INTEGER NOT NULL DEFAULT 0")
    private boolean addressVerified = false;

    private LocalDateTime createdAt = LocalDateTime.now();

    @PrePersist
    @PreUpdate
    private void ensureName() {
        if (name == null || name.isBlank()) {
            if (fullName != null && !fullName.isBlank()) {
                name = fullName;
            } else if (email != null && !email.isBlank()) {
                name = email;
            } else if (phone != null && !phone.isBlank()) {
                name = phone;
            } else {
                name = "Miembro";
            }
        }
    }

    public Member() {
    }

    public Member(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public Long getId() { return id; }

    public String getName() { return name; }

    public Member setName(String name) {
        this.name = name;
        return this;
    }

    public String getFullName() { return fullName; }

    public Member setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public String getPhone() { return phone; }

    public Member setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getEmail() { return email; }

    public Member setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getAddress() { return address; }

    public Member setAddress(String address) {
        this.address = address;
        return this;
    }

    public boolean isEmailVerified() { return emailVerified; }

    public Member setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
        return this;
    }

    public boolean isAddressVerified() { return addressVerified; }

    public Member setAddressVerified(boolean addressVerified) {
        this.addressVerified = addressVerified;
        return this;
    }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public Member setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}
