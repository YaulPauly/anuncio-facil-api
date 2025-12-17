package com.cibertec.anuncio_facil_api.application.dto.response;

public record BasicUserResponse(
        Long id,
        String email,
        String firstName,
        String lastName
) {
}
