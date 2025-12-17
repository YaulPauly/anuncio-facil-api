package com.cibertec.anuncio_facil_api.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AdRequest(
        @NotBlank String title,
        @NotBlank String description,
        String image,
        @NotBlank String city,
        @NotBlank String district,
        @NotNull Long categoryId,
        String detail
) {
}
