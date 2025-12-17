package com.cibertec.anuncio_facil_api.application.dto.response;

public record AuthResponse(
        String token,
        String email,
        String firstName,
        String lastName,
        String role
) {
}
