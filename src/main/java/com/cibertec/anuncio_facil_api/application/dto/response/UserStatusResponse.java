package com.cibertec.anuncio_facil_api.application.dto.response;

import com.cibertec.anuncio_facil_api.domain.model.UserStatus;

public record UserStatusResponse(
        Long id,
        String email,
        UserStatus status,
        String message
) {
}
