package com.cibertec.anuncio_facil_api.infrastructure.controller;

import com.cibertec.anuncio_facil_api.application.dto.request.AdRequest;
import com.cibertec.anuncio_facil_api.application.dto.response.AdResponse;
import com.cibertec.anuncio_facil_api.application.usecases.interfaces.AdUseCase;
import com.cibertec.anuncio_facil_api.application.service.MediaStorageService;
import com.cibertec.anuncio_facil_api.domain.model.AdStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
public class AdsController {

    private final AdUseCase adUseCase;
    private final MediaStorageService mediaStorageService;

    @PostMapping(consumes = {"application/json", "multipart/form-data"})
    public ResponseEntity<AdResponse> create(
            @RequestPart("ad") @Valid AdRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        AdRequest finalRequest = withUploadedImage(request, file);
        return ResponseEntity.ok(adUseCase.create(finalRequest));
    }

    @PutMapping(value = "/{id}", consumes = {"application/json", "multipart/form-data"})
    public ResponseEntity<AdResponse> update(@PathVariable Long id,
                                             @RequestPart("ad") @Valid AdRequest request,
                                             @RequestPart(value = "file", required = false) MultipartFile file) {
        AdRequest finalRequest = withUploadedImage(request, file);
        return ResponseEntity.ok(adUseCase.update(id, finalRequest));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AdResponse> toggleStatus(@PathVariable Long id, @RequestParam AdStatus status) {
        return ResponseEntity.ok(adUseCase.toggleStatus(id, status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(adUseCase.getById(id));
    }

    @GetMapping
    public ResponseEntity<com.cibertec.anuncio_facil_api.application.dto.response.PagedResponse<AdResponse>> list(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String district,
            @RequestParam(required = false) AdStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(adUseCase.list(categoryId, city, district, status, page, size));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<AdResponse>> listMine() {
        return ResponseEntity.ok(adUseCase.listByCurrentUser());
    }

    private AdRequest withUploadedImage(AdRequest request, MultipartFile file) {
        String imageUrl = request.image();
        if (file != null && !file.isEmpty()) {
            imageUrl = mediaStorageService.upload(file);
        }
        return new AdRequest(
                request.title(),
                request.description(),
                imageUrl,
                request.city(),
                request.district(),
                request.categoryId(),
                request.detail()
        );
    }
}
