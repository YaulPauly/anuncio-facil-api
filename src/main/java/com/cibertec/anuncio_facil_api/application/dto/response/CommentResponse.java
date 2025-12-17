package com.cibertec.anuncio_facil_api.application.dto.response;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String content,
        BasicUserResponse user,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
