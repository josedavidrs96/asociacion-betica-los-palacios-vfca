package com.pena.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootApplication
public class PenaBeticaApplication {
    public static void main(String[] args) {
        ensureDataDir();
        SpringApplication.run(PenaBeticaApplication.class, args);
    }

    private static void ensureDataDir() {
        try {
            Path data = Path.of("data");
            if (Files.notExists(data)) {
                Files.createDirectories(data);
            }
        } catch (IOException e) {
            System.err.println("WARN: cannot create 'data' directory: " + e.getMessage());
        }
    }
}
