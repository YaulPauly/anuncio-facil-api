package com.cibertec.anuncio_facil_api.application.service;

import org.springframework.web.multipart.MultipartFile;

public interface MediaStorageService {
    String upload(MultipartFile file);
}
