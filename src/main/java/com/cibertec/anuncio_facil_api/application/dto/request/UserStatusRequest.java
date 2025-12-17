package com.cibertec.anuncio_facil_api.application.dto.request;

import com.cibertec.anuncio_facil_api.domain.model.UserStatus;
import jakarta.validation.constraints.NotNull;

public record UserStatusRequest(@NotNull UserStatus status) {
}
