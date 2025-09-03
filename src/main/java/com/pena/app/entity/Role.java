package com.pena.app.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String name; // e.g. ADMIN, ABONADO

    public Role() {}

    public Role(String name) {
        this.name = name;
    }

    public Long getId() { return id; }

    public String getName() { return name; }

    public Role setName(String name) {
        this.name = name;
        return this;
    }
}
