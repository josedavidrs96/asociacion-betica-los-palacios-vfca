package com.pena.app.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.concurrent.TimeUnit;

@Controller
public class ImageController {

    @GetMapping(value = "/escudobetis.png", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<Resource> escudo() {
        // Intentar en varias rutas típicas del classpath
        String[] candidates = new String[] {
                "escudobetis.png",
                "static/escudobetis.png",
                "static/images/escudobetis.png",
                "public/escudobetis.png"
        };
        for (String path : candidates) {
            Resource candidate = new ClassPathResource(path);
            if (candidate.exists()) {
                return ResponseEntity.ok()
                        .cacheControl(CacheControl.maxAge(365, TimeUnit.DAYS).cachePublic())
                        .body(candidate);
            }
        }
        return ResponseEntity.notFound().build();
    }
}
