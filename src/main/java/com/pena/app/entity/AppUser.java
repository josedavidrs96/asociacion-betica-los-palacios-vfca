package com.pena.app.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class AppUser {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 120)
    private String username; // email

        @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 1")
    private String password;

    // Nuevo: habilitado/inicio de sesión permitido
    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 1")
    private boolean enabled = true;

    @OneToOne(optional = true)
    private Member member;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns =  @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public AppUser() {}

    public AppUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Long getId() { return id; }

    public String getUsername() { return username; }

    public AppUser setUsername(String username) {
        this.username = username; return this;
    }

    public String getPassword() { return password; }

    public AppUser setPassword(String password) {
        this.password = password; return this;
    }

    public boolean isEnabled() { return enabled; }

    public AppUser setEnabled(boolean enabled) {
        this.enabled = enabled; return this;
    }

    public Member getMember() { return member; }

    public AppUser setMember(Member member) {
        this.member = member; return this;
    }

    public Set<Role> getRoles() { return roles; }

    public AppUser setRoles(Set<Role> roles) {
        this.roles = roles; return this;
    }

    public AppUser addRole(Role role) { this.roles.add(role); return this; }
}
