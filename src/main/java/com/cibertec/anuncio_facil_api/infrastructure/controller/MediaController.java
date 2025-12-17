package com.cibertec.anuncio_facil_api.infrastructure.controller;

import com.cibertec.anuncio_facil_api.application.service.MediaStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaStorageService mediaStorageService;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Map<String, String>> upload(@RequestPart("file") MultipartFile file) {
        String url = mediaStorageService.upload(file);
        return ResponseEntity.ok(Map.of("url", url));
    }
}
