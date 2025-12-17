package com.cibertec.anuncio_facil_api.domain.service.interfaces;

import com.cibertec.anuncio_facil_api.domain.model.Ad;
import com.cibertec.anuncio_facil_api.domain.model.AdStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AdRepository {
    Ad save(Ad ad);
    Optional<Ad> findById(Long id);
    List<Ad> findAll();
    List<Ad> findByUserId(Long userId);
    Page<Ad> findByFilters(Long categoryId, String city, String district, AdStatus status, Pageable pageable);
}
