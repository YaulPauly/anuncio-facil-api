package com.cibertec.anuncio_facil_api.application.dto.response;

import com.cibertec.anuncio_facil_api.domain.model.UserStatus;

public record UserResponse(
        Long id,
        String email,
        String firstName,
        String lastName,
        String role,
        UserStatus status
) {
}
