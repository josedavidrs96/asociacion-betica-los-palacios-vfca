package com.pena.app.config;

import com.pena.app.entity.AppUser;
import com.pena.app.entity.Role;
import com.pena.app.repository.AppUserRepository;
import com.pena.app.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.annotation.Order;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    @Order(1)
    CommandLineRunner initData(RoleRepository roleRepository,
                               AppUserRepository appUserRepository,
                               PasswordEncoder encoder) {
        return args -> {
            Role admin = roleRepository.findByName("ADMIN").orElseGet(() -> roleRepository.save(new Role("ADMIN")));
            Role abonado = roleRepository.findByName("ABONADO").orElseGet(() -> roleRepository.save(new Role("ABONADO")));

            if (appUserRepository.findByUsername("admin@betica.local").isEmpty()) {
                AppUser u = new AppUser("admin@betica.local", encoder.encode("admin123"));
                u.setEnabled(true);
                u.addRole(admin);
                appUserRepository.save(u);
                System.out.println("Usuario admin creado: admin@betica.local / admin123");
            }
        };
    }
}
