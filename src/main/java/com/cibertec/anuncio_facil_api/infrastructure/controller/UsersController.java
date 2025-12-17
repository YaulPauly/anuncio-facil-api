package com.cibertec.anuncio_facil_api.infrastructure.controller;

import com.cibertec.anuncio_facil_api.application.dto.request.UserStatusRequest;
import com.cibertec.anuncio_facil_api.application.dto.response.UserStatusResponse;
import com.cibertec.anuncio_facil_api.application.usecases.interfaces.UserUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {

    private final UserUseCase userUseCase;

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<UserStatusResponse> updateStatus(@PathVariable Long id,
                                                           @Valid @RequestBody UserStatusRequest request) {
        return ResponseEntity.ok(userUseCase.updateStatus(id, request));
    }
}
