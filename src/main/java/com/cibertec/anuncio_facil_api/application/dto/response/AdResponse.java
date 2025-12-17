package com.cibertec.anuncio_facil_api.application.dto.response;

import com.cibertec.anuncio_facil_api.domain.model.AdStatus;

import java.time.LocalDateTime;

public record AdResponse(
        Long id,
        String title,
        String description,
        String image,
        String city,
        String district,
        AdStatus status,
        CategoryResponse category,
        BasicUserResponse user,
        String detail,
        LocalDateTime createdAt
) {
}
